package com.scf.erdos.factoring.service.impl;

import com.scf.erdos.common.auth.details.LoginAppUser;
import com.scf.erdos.common.exception.service.ServiceException;
import com.scf.erdos.common.util.StringUtil;
import com.scf.erdos.common.util.SysUserUtil;
import com.scf.erdos.common.web.PageResult;
import com.scf.erdos.common.web.Result;
import com.scf.erdos.factoring.dao.CompanyDao;
import com.scf.erdos.factoring.dao.FinancingStartDao;
import com.scf.erdos.factoring.dao.ProductDao;
import com.scf.erdos.factoring.dao.ReceivableDao;
import com.scf.erdos.factoring.model.financing.Financing;
import com.scf.erdos.factoring.model.financing.FinancingSave;
import com.scf.erdos.factoring.service.ICompanyService;
import com.scf.erdos.factoring.service.IFinancingStartService;
import com.scf.erdos.factoring.vo.company.CompanyInfoVo;
import com.scf.erdos.factoring.vo.dict.CompanyVo;
import com.scf.erdos.factoring.vo.financing.AppliFinancingVo;
import com.scf.erdos.factoring.vo.financing.ProductCredits;
import com.scf.erdos.factoring.vo.financing.ProductsVo;
import com.scf.erdos.factoring.vo.financing.Receivable;
import com.scf.erdos.factoring.vo.product.ProductInfoVo;
import com.scf.erdos.factoring.vo.yszk.ReceivableInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @Description : 融资申请发起
 * @author：bao-clm
 * @date: 2020/7/29
 * @version：1.0
 */

@Slf4j
@Service
@SuppressWarnings("all")
public class FinancingStartServiceImpl implements IFinancingStartService {

    @Autowired
    private FinancingStartDao financingStartDao;
    @Autowired
    private CompanyDao companyDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ReceivableDao receivableDao;

    @Override
    public Result getProductCredit(Map<String, Object> params) throws ServiceException {
        //1，获取当前用户信息
        LoginAppUser loginUser = SysUserUtil.getLoginAppUser();
        if(loginUser == null)
            return Result.failed("获取当前用户登陆信息失败");
        params.put("userId",loginUser.getId());
        params.put("companyId",loginUser.getCompanyId());//用户企业id

        List<ProductCredits> list = financingStartDao.getProductCredits(params);
        return Result.succeed(list,"成功");
    }

    @Override
    public Result getProducts(Map<String, Object> params) throws ServiceException {

        try {
            //1，获取当前用户信息
            LoginAppUser loginUser = SysUserUtil.getLoginAppUser();
            if(loginUser == null)
                return Result.failed("获取当前用户登陆信息失败");
            params.put("userId",loginUser.getId());
            params.put("companyId",loginUser.getCompanyId());//用户企业id

            Integer page = MapUtils.getInteger(params, "page");
            Integer limit = MapUtils.getInteger(params, "limit");

            if (page == null || limit == null) {
                return Result.failed("page，limit不能为空！");
            }

            if(page < 1){
                return Result.failed("page不能小于1，首页从1开始！");
            }

            params.put("currentPage",(page - 1)*limit);
            params.put("pageSize",limit);

            int total = financingStartDao.count(params);
            List<ProductsVo> list = financingStartDao.getFinancingProducts(params);

            PageResult pageResult = PageResult.<ProductsVo>builder().page(page).limit(limit).data(list).count((long)total).build();
            return Result.succeed(pageResult,"成功");
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Result applyFinancing(Map<String, Object> params) throws ServiceException {
        try {
            //1，获取当前用户信息
            LoginAppUser loginUser = SysUserUtil.getLoginAppUser();
            if(loginUser == null)
                return Result.failed("获取当前用户登陆信息失败");
            params.put("userId",loginUser.getId());
            params.put("id",loginUser.getCompanyId());//用户企业id ---- 获取企业详情接口 参数 id
            params.put("customerCompanyId",loginUser.getCompanyId());//用户企业id

            CompanyInfoVo companyInfoVo = companyDao.getCompanyInfo(params);
            int productId = Integer.parseInt(params.get("productId").toString());
            ProductInfoVo productInfoVo = productDao.getProductInfo(productId);
            //2，产品是否需要在线开户
            if(productInfoVo.getIsPlatformAccount() && StringUtil.isEmpty(companyInfoVo.getCompanyAccount())){
                Result.failedWith(null,-1001,"请您先在【企业信息】页面完成在线开户！");
            }

            /**
             * 3，产品授信情况
             * 状态（0，待初审；1，待审核；2，待审批；3，生效；4，作废；5，已到期）
             *
             * 点击“融资申请”按钮时，如果该产品的授信已到期，则系统提示该产品授信已到期，请先进行【授信申请】！：
             *
             * 点击“融资申请”按钮时，如果该产品的授信未到期，且授信额度不为0时，直接进进入融资申请页面，
             * 但进入到融资申请页面后客户输入的融资申请金额＞授信剩余可用额度时，系统提示“该产品授信额度不足，
             * 请联系资金机构！”，并且不允许提交。
             *
             * 点击“融资申请”按钮时，如果该产品的授信未到期，且授信额度为0时，系统提示“该产品授信额度不足，请联系资金机构！”。
             *
             * 点击“融资申请”按钮时，如果该产品没有授信或授信已到期，则系统提示“请您先进行授信申请！”。
             */
            ProductCredits creditStatus = financingStartDao.getCreditStatus(params);

            if("012".contains(creditStatus.getStatus())){
                Result.failedWith(null,-1002,"该产品授信申请正在审批阶段，不能融资申请！");
            }

            if("3".equals(creditStatus.getStatus())){
                double availableCreditLine = creditStatus.getAvailableCreditLine();
                if(!(availableCreditLine > 0 )){
                    Result.failedWith(null,-1003,"该产品授信额度不足，请联系资金机构！");
                }
            }

            if("4".equals(creditStatus.getStatus()) || creditStatus.getStatus() == null){
                Result.failedWith(null,-1004,"产品未授信申请，请先进行【授信申请】！");
            }

            if("5".equals(creditStatus.getStatus())){
                Result.failedWith(null,-1005,"该产品授信申请正在审批阶段，不能融资申请！");
            }

            //4，返回融资申请页面参数对象
            AppliFinancingVo appliFinancingVo = financingStartDao.getAppliFinancingVo(productId);
            List<Receivable> receivableList = financingStartDao.getReceivable(loginUser.getCompanyId());
            /**
             * 获取是否明暗保理 flowType
             * product_dict_list  type = "p009"
             *      code = "1000"  || "1001"  -> 明保理
             */
            appliFinancingVo.setFlowType(productInfoVo.getFlowType());//明暗保理标志
            appliFinancingVo.setFinancingRate(productInfoVo.getFinancingRate());//融资比例
            appliFinancingVo.setReceivableList(receivableList);
            return Result.succeed(appliFinancingVo,"成功");
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional
    public Result save(FinancingSave financingSave) throws ServiceException {
        try {
            // 获取当前用户登陆信息
            LoginAppUser loginUser = SysUserUtil.getLoginAppUser();
            if(loginUser == null)
                return Result.failed("获取当前用户登陆信息失败");
            financingSave.setUserId(loginUser.getId());
            financingSave.setCompanyId(loginUser.getCompanyId());

            // 本次融资应收账款信息
            ReceivableInfoVo yszkInfoVo = receivableDao.getYszkInfo(financingSave.getReceivableId());
            String rBuyerCompany = yszkInfoVo.getBuyerCompany();//应收账款买方企业名称
            double yszkAmount = yszkInfoVo.getYszkAmount();//应收账款金额/待转让金额
            Boolean status = yszkInfoVo.getStatus();//应收账款使用状态
            if(status){
                return Result.failedWith(null,-1001,"该应收账款已融资，不能再次融资");
            }else{
                // 本次融资产品信息
                int productId = Integer.parseInt(financingSave.getProductId());
                ProductInfoVo productInfoVo = productDao.getProductInfo(productId);
                financingSave.setFundingCompanyId(productInfoVo.getCompanyId());//资金机构id
                int financingRate = productInfoVo.getFinancingRate();//融资比例

                Double amountTransfer = financingSave.getAmountTransfer();//总转让金额
                Double web_amountFinancing = financingSave.getAmountTransfer();//融资金额（页面提交数据）
                Double back_amountFinancing = amountTransfer * financingRate * 0.01;//融资金额（后台计算数据）
                if(amountTransfer > yszkAmount){
                    return Result.failedWith(null,-1002,"转让金额不能大于应收账款金额！");
                }
                if(back_amountFinancing > amountTransfer){
                    return Result.failedWith(null,-1003,"融资金额不能大于转让金额！");
                }
                financingSave.setAmountFinancing(back_amountFinancing);

                /**
                 * flowType :
                 * product_dict_list 表 type = "p009"
                 * 确权-免确认挂账/确权-需确认挂账（明保理）；无确权（暗保理）
                 */
                String flowType = productInfoVo.getFlowType();
                if(!"1002".equals(flowType)){
                    /**
                     * 明保理 - 融资申请时必填买方企业名称
                     *          而且买方企业跟应收账款的买方企业是同一家
                     */
                    if(financingSave.getBuyerCompanyName() != null){
                        if(!rBuyerCompany.equals(financingSave.getBuyerCompanyName())){
                            return Result.failedWith(null,-1004,"明保理-融资申请时必填买方企业名称，而且买方企业跟应收账款的买方企业是同一家");
                        }
                    }else{
                        return Result.failedWith(null,-1005,"明保理时买方企业不能为空");
                    }
                }
            }

            //3，生成融资编号 RX + 年月日 + 7随机数
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String date = now.format(formatter);
            String code = "";
            String reCode = "";
            do{
                code = "RX" + date + ((int)(new Random().nextDouble()*(9999999-1000000 + 1))+ 1000000);
                reCode = financingStartDao.getCode(code);
            }while(!StringUtils.isBlank(reCode));
            financingSave.setCode(code);//融资申请编号

            //4，保存融资信息，修改应收账款使用状态
            financingStartDao.saveFinancingInfo(financingSave);
            financingStartDao.updateReceivable(financingSave);//修改应收账款（已被使用）
            return Result.succeed("成功");
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Result getBuyerCompanys(Map<String, Object> params) throws ServiceException {
        List<CompanyVo> list = financingStartDao.getBuyerCompanys();
        return Result.succeed(list,"成功");
    }
}
