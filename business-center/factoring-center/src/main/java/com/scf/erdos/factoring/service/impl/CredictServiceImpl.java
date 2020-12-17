package com.scf.erdos.factoring.service.impl;

import com.scf.erdos.common.auth.details.LoginAppUser;
import com.scf.erdos.common.constant.CompanyRole;
import com.scf.erdos.common.exception.service.ServiceException;
import com.scf.erdos.common.model.SysRole;
import com.scf.erdos.common.model.SysUser;
import com.scf.erdos.common.util.SysUserUtil;
import com.scf.erdos.common.web.PageParamHandle;
import com.scf.erdos.common.web.PageResult;
import com.scf.erdos.common.web.Result;
import com.scf.erdos.factoring.dao.CompanyDao;
import com.scf.erdos.factoring.dao.CredictDao;
import com.scf.erdos.factoring.dao.ProductDao;
import com.scf.erdos.factoring.feign.UserFeignClient;
import com.scf.erdos.factoring.model.credit.ProductCredit;
import com.scf.erdos.factoring.service.ICredictService;
import com.scf.erdos.factoring.vo.company.CompanyInfoVo;
import com.scf.erdos.factoring.vo.credit.CreditVo;
import com.scf.erdos.factoring.vo.credit.CreditPageVo;
import com.scf.erdos.factoring.vo.credit.ProductCreditLog;
import com.scf.erdos.factoring.vo.product.ProductContractsVo;
import com.scf.erdos.factoring.vo.product.ProductInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description : 授信服务实现
 * @author：bao-clm
 * @date: 2020/6/8
 * @version：1.0
 */

@Slf4j
@Service
@SuppressWarnings("all")
public class CredictServiceImpl implements ICredictService {

    @Autowired
    private CredictDao credictDao;
    @Autowired
    private CompanyDao companyDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private PageParamHandle pageParamHandle;

    @Override
    public Result apply(Map<String, Object> params) throws ServiceException {
        try {
            //1，获取当前用户信息
            LoginAppUser loginUser = SysUserUtil.getLoginAppUser();
            if(loginUser == null)
                return Result.failed("获取当前用户登陆信息失败");
            params.put("userId",loginUser.getId());
            params.put("companyId",loginUser.getCompanyId());

            //2，产品不需要授信
            boolean isCredit = credictDao.getIsCreditByProductId(params.get("productId").toString());
            if(!isCredit){
                return Result.failedWith(null,-1001,"产品不需要授信");
            }

            //3，不能重复授信
            String creditCode = credictDao.getCreditCodeById(params);
            if(StringUtils.isNotEmpty(creditCode)){
                return Result.failedWith(null,-1002,"不能重复申请");
            }

            //4，授信编号 RX + 年月日 + 7随机数
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String date = now.format(formatter);
            String code = "";
            String reCode = "";
            do{
                code = "RX" + date + ((int)(new Random().nextDouble()*(9999999-1000000 + 1))+ 1000000);
                reCode = credictDao.getCode(code);
            }while(!StringUtils.isBlank(reCode));

            params.put("code",code);
            credictDao.apply(params);

            return Result.succeed(code,"成功");
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Result getCredicts(Map<String, Object> map) throws ServiceException {
        try {
            Result result = pageParamHandle.handle(map);
            if(200 == result.getResp_code()){
                Map<String, Object> param = (Map<String, Object>)result.getData();
                //1，获取当前用户信息
                LoginAppUser loginUser = SysUserUtil.getLoginAppUser();
                if(loginUser == null)
                    return Result.failed("获取当前用户登陆信息失败");
                Set<SysRole> roles = loginUser.getSysRoles();
                List<SysRole> rolesList = new ArrayList(roles);
                String roleCode = rolesList.get(0).getCode();//获取角色code，每个用户只有一个角色

                param.put("userId",loginUser.getId());
                param.put("id",loginUser.getCompanyId());//用户企业id
                CompanyInfoVo companyInfoVo = companyDao.getCompanyInfo(param);
                String companyLabel1 = companyInfoVo.getCompanyLabel1();//企业标签

                /**
                 * 2，资金机构/一般企业
                 */
                if(companyLabel1.equals(CompanyRole.COMPANY_10004)){
                    //资金机构
                    param.put("fundingCompanyId",loginUser.getCompanyId());

                }else{
                    //客户机构
                    param.put("customerCompanyId",loginUser.getCompanyId());
                }

                int total = credictDao.count(param);
                List<CreditPageVo> list = credictDao.getCredicts(param);
                List<String> userIds = list.stream().map(CreditPageVo::getUserId).collect(Collectors.toList());

                List<CreditPageVo> newList = new ArrayList<CreditPageVo>();
                if(userIds.size() > 0){
                    //获取用户信息
                    List<SysUser> userList = userFeignClient.findByUserIds(userIds);
                    //用户信息组装
                    newList = list.stream()
                            .map(creditPageVo -> userList.stream()
                                    .filter(loginAppUser -> creditPageVo.getUserId().equals(String.valueOf(loginAppUser.getId())))
                                    .findFirst()
                                    .map(loginAppUser -> {
                                        creditPageVo.setCustomerName(loginAppUser.getRealname());

                                        //按钮权限设置
                                        creditPageVo.setStatusActive(true);

                                        return creditPageVo;
                                    }).orElse(null))
                            .collect(Collectors.toList());
                }

                int page = Integer.parseInt(param.get("page").toString());
                int limit = Integer.parseInt(param.get("limit").toString());
                PageResult pageResult = PageResult.<CreditPageVo>builder().page(page).limit(limit).data(newList).count((long)total).build();
                return Result.succeed(pageResult,"成功");

            }else{
                return result;
            }
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional
    public Result getCreditInfo(Map<String, Object> params) throws ServiceException {
        try {
            //1，授信申请详情
            Integer id = Integer.parseInt(params.get("id").toString());
            CreditVo creditVo = credictDao.getCreditVoById(id);

            //2，申请人企业详情
            LoginAppUser loginUser = SysUserUtil.getLoginAppUser();
            params.put("id",loginUser.getCompanyId());
            CompanyInfoVo companyInfoVo = companyDao.getCompanyInfo(params);

            BeanUtils.copyProperties(companyInfoVo,creditVo);
            creditVo.setCustomerCompanyId(companyInfoVo.getId());
            creditVo.setCustomerCompany(companyInfoVo.getCompanyName());
            creditVo.setIndustryTypeName(companyInfoVo.getIndustryTypeName());
            creditVo.setBusinessScale(companyInfoVo.getBusinessScale());

            String registeredTime = companyInfoVo.getRegisteredTime();
            if(StringUtils.isNotEmpty(registeredTime)){
                creditVo.setRegisteredYears(getRegisteredYear(companyInfoVo.getRegisteredTime()));
            }

            //3，获取授信申请审核日志，一般企业不显示
            String companyLabel1 = companyInfoVo.getCompanyLabel1();//企业标签
            if(companyLabel1.equals(CompanyRole.COMPANY_10004)){
                List<ProductCreditLog> productCreditLogList = companyDao.getAuditLogs(id);
                List<String> userIds = productCreditLogList.stream().map(ProductCreditLog::getUserId).distinct().collect(Collectors.toList());
                List<ProductCreditLog> newList = new ArrayList<ProductCreditLog>();
                if(userIds.size() > 0){
                    //获取用户信息
                    List<SysUser> userList = userFeignClient.findByUserIds(userIds);
                    //用户信息组装
                    newList = productCreditLogList.stream()
                            .map(productCreditLog -> userList.stream()
                                    .filter(loginAppUser -> productCreditLog.getUserId().equals(String.valueOf(loginAppUser.getId())))
                                    .findFirst()
                                    .map(loginAppUser -> {
                                        productCreditLog.setUserName(loginAppUser.getRealname());
                                        return productCreditLog;
                                    }).orElse(null))
                            .collect(Collectors.toList());
                }
                creditVo.setProductCreditLogList(newList);
            }

            return Result.succeed(creditVo,"成功");
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional
    public Result audit(ProductCredit productCredit) throws ServiceException {
        try {
            //1，获取当前用户信息
            LoginAppUser loginUser = SysUserUtil.getLoginAppUser();
            if(loginUser == null)
                return Result.failed("获取当前用户登陆信息失败");
            productCredit.setUserId(loginUser.getId());//审核人id

            Set<SysRole> roles = loginUser.getSysRoles();
            List<SysRole> rolesList = new ArrayList(roles);
            String roleCode = rolesList.get(0).getCode();//获取角色code，每个用户只有一个角色

            /**
             *  状态（0，待初审；1，待审核；2，待审批；3，生效；4，作废；5，已到期）
             *
             *  业务受理岗    初审
             *  风控审核岗    审核
             *  决策岗        审批
             *
             *  初审通过 - > 到（1，待审核） - > 到（2，待审批） - >  到（3，生效）
             *
             *  业务受理岗 驳回 - > 授信申请作废
             *  风控审核岗 驳回 - > 0，待初审
             *  决策岗 驳回 - > 1，待审核
             */
            CreditVo creditVo = credictDao.getCreditVoById(productCredit.getId());
            int creditStatus = creditVo.getStatus();//授信申请状态
            int isCredit = creditVo.getIsCredit();//授信流程（1，一级；2，二级；3，三级）
            String status = productCredit.getStatus();//操作员操作状态（1，同意； 2，驳回）

            if(creditStatus == 3 || creditStatus == 4 || creditStatus == 5){
                return Result.failedWith(null,-1001,"授信申请已生效/作废/已到期，不能再审核");
            }

            boolean bl = false;
            String str = "";

            /**
             * 1，一级审批
             */
            if(isCredit == 1){
                //资金方 - 业务受理岗 - 初审  - > 生效/作废
                if("0".equals(creditStatus) && roleCode.equals(CompanyRole.COMPANY_10004_01_ROLE)){
                    str = "1".equals(status) ?  "3":  "4";
                    bl = true;
                }
            }

            /**
             * 2，二级审批
             */
            if(isCredit == 2){
                //资金方 - 业务受理岗 - 初审  - > 待审核
                if("0".equals(creditStatus) && roleCode.equals(CompanyRole.COMPANY_10004_01_ROLE)){
                    str = "1".equals(status) ?  "1":  "0";
                    bl = true;
                }
                //资金方 - 风控审核岗 - 审核  - > 生效/作废
                if("1".equals(creditStatus) && roleCode.equals(CompanyRole.COMPANY_10004_02_ROLE)){
                    str = "1".equals(status) ?  "3":  "4";
                    bl = true;
                }
            }

            /**
             * 3，三级审批
             */
            if(isCredit == 3) {
                //资金方 - 业务受理岗 - 初审  - > 待审核
                if ("0".equals(creditStatus) && roleCode.equals(CompanyRole.COMPANY_10004_01_ROLE)) {
                    str = "1".equals(status) ? "1" : "4";
                    bl = true;
                }
                //资金方 - 风控审核岗 - 审核  - > 待审批
                if ("1".equals(creditStatus) && roleCode.equals(CompanyRole.COMPANY_10004_02_ROLE)) {
                    str = "1".equals(status) ? "2" : "0";
                    bl = true;
                }
                //资金方 - 决策岗 - 审批  - > 生效
                if ("2".equals(creditStatus) && roleCode.equals(CompanyRole.COMPANY_10004_03_ROLE)) {
                    str = "1".equals(status) ? "3" : "1";
                    bl = true;
                }
            }

            if(bl){
                productCredit.setCreditStatus(str);
                credictDao.saveAuditLog(productCredit);
                credictDao.update(productCredit);

                /**
                 * 产品授信生效时生成合同
                 */
                Integer productId = creditVo.getProductId();
                if("3".equals(str)){
                    List<ProductContractsVo> list = credictDao.getContracts(productId);
                }
                return Result.succeed("成功");
            }else{
                return Result.failedWith(null,-1002,"岗位和操作内容不符");
            }
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    /**
     * 获取企业注册年限
     */
    public String getRegisteredYear(String registeredTime) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//注意月份是MM
        Date date = simpleDateFormat.parse(registeredTime);

        // 当前时间
        Calendar curr = Calendar.getInstance();
        // 注册时间
        Calendar registedTime = Calendar.getInstance();
        registedTime.setTime(date);
        // 注册年限 = 当前年 - 注册时间
        int registeredYear = curr.get(Calendar.YEAR) - registedTime.get(Calendar.YEAR);

        /*// 如果当前月份小于注册月份: registeredYear-1
        // 如果当前月份等于住蹙额月份, 且当前日小于注册日: registeredYear-1
        int currMonth = curr.get(Calendar.MONTH);
        int currDay = curr.get(Calendar.DAY_OF_MONTH);
        int registerMonth = registedTime.get(Calendar.MONTH);
        int registerDay = registedTime.get(Calendar.DAY_OF_MONTH);
        if ((currMonth < registerMonth) || (currMonth == registerMonth && currDay <= registerDay)) {
            registeredYear--;
        }*/

        return registeredYear + "";
    }

}
