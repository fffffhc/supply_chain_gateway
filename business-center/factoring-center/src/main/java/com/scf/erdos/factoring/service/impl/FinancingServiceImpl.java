package com.scf.erdos.factoring.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.scf.erdos.common.auth.details.LoginAppUser;
import com.scf.erdos.common.constant.CompanyRole;
import com.scf.erdos.common.exception.service.ServiceException;
import com.scf.erdos.common.model.SysRole;
import com.scf.erdos.common.model.SysUser;
import com.scf.erdos.common.util.SysUserUtil;
import com.scf.erdos.common.web.CodeEnum;
import com.scf.erdos.common.web.PageParamHandle;
import com.scf.erdos.common.web.PageResult;
import com.scf.erdos.common.web.Result;
import com.scf.erdos.factoring.contract.server.ProduceContractService;
import com.scf.erdos.factoring.dao.*;
import com.scf.erdos.factoring.feign.UserFeignClient;
import com.scf.erdos.factoring.model.financing.Financing;
import com.scf.erdos.factoring.service.IFinancingService;
import com.scf.erdos.factoring.util.financialUtils.CalculatingMoney;
import com.scf.erdos.factoring.vo.company.CompanyInfoVo;
import com.scf.erdos.factoring.vo.credit.CreditVo;
import com.scf.erdos.factoring.vo.financing.*;
import com.scf.erdos.factoring.vo.pay.PaymentNoteVo;
import com.scf.erdos.factoring.vo.product.ProductInfoVo;
import com.scf.erdos.factoring.vo.workflow.CompanyWorkflow;
import com.scf.erdos.factoring.vo.workflow.WorkflowInfoVo;
import com.scf.erdos.factoring.vo.yszk.ReceivableInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Description : 融资申请 service 实现类
 * @author：bao-clm
 * @date: 2020/6/8
 * @version：1.0
 */

@Slf4j
@Service
@SuppressWarnings("all")
public class FinancingServiceImpl implements IFinancingService {

    @Autowired
    private FinancingDao financingDao;

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private PageParamHandle pageParamHandle;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ReceivableDao receivableDao;

    @Autowired
    private FinancingStartDao financingStartDao;

    @Autowired
    private ProduceContractService contractTemplateService;

    @Override
    public Result getFinancings(Map<String, Object> map) throws ServiceException {
        try {
            Result result = pageParamHandle.handle(map);
            if(200 == result.getResp_code()){
                Map<String, Object> param = (Map<String, Object>)result.getData();
                //1，获取当前用户信息
                LoginAppUser loginUser = SysUserUtil.getLoginAppUser();
                if(loginUser == null)
                    return Result.failed("获取当前用户登陆信息失败");
                param.put("userId",loginUser.getId());
                param.put("id",loginUser.getCompanyId());//用户企业id
                CompanyInfoVo companyInfoVo = companyDao.getCompanyInfo(param);
                String companyLabel1 = companyInfoVo.getCompanyLabel1();//企业标签

                Set<SysRole> roles = loginUser.getSysRoles();
                List<SysRole> roleList = new ArrayList(roles);
                String roleCode = roleList.get(0).getCode();//用户角色code，系统用户只有一个角色

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

                int total = financingDao.count(param);
                List<FinancingPageVo> list = financingDao.getFinancingPages(param);

                List<FinancingPageVo> newList = new ArrayList<>();
                for(FinancingPageVo financingPageVo : list){
                    String flowInfo = financingPageVo.getFlowInfo();//工作流审批json 包
                    int status = financingPageVo.getStatus();//融资状态
                    List<CompanyWorkflow> rolesList = JSONObject.parseArray(flowInfo, CompanyWorkflow.class);
                    for(CompanyWorkflow companyWorkflow : rolesList){
                        String code = companyWorkflow.getCode();//工作流角色code
                        int wStatus = companyWorkflow.getStatus();//工作流操作状态
                        if(roleCode.equals(code) && status == wStatus){
                            financingPageVo.setStatusActive(true);
                        }else{
                            financingPageVo.setStatusActive(false);
                        }
                    }
                    financingPageVo.setCompanyLabel1(companyLabel1);//企业标签
                    financingPageVo.setFlowInfo(null);//清除工作流详情
                    newList.add(financingPageVo);
                }
                int page = Integer.parseInt(param.get("page").toString());
                int limit = Integer.parseInt(param.get("limit").toString());
                PageResult pageResult = PageResult.<FinancingPageVo>builder().page(page).limit(limit).data(newList).count((long)total).build();
                return Result.succeed(pageResult,"成功");
            }else{
                return result;
            }
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Result getFinancingInfo(Integer id) throws ServiceException {
        try {
            FinancingInfoVo financingInfo = financingDao.getFinancingInfo(id);
            //1，产品详情
            Integer productId = financingInfo.getProductId();
            ProductInfoVo productInfoVo = productDao.getProductInfo(productId);
            BeanUtils.copyProperties(productInfoVo,financingInfo);

            //2，应收账款详情
            Integer receivableId= financingInfo.getReceivableId();
            ReceivableInfoVo yszkInfoVo = receivableDao.getYszkInfo(receivableId);
            BeanUtils.copyProperties(yszkInfoVo,financingInfo);
            financingInfo.setBuyerCompanyName(yszkInfoVo.getBuyerCompany());
            financingInfo.setContractList(receivableDao.getContractsByYszkId(receivableId));
            financingInfo.setFapiaoList(receivableDao.getFapiaoByYszkId(receivableId));
            financingInfo.setOtherBillList(receivableDao.getOtherBillByYszkId(receivableId));

            return Result.succeed(financingInfo,"成功");
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Result getCustomerInfo(Integer id) throws ServiceException {
        try {
            CompanyInfoVo companyInfoVo = financingDao.getCompanyInfo(id);
            return Result.succeed(companyInfoVo,"成功");
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Result financeingReview(Integer id) throws ServiceException {
        try {
            //获取融资申请详情
            DecisionSubmissionVo decisionSubmissionVo = financingDao.getDecisionSubmission(id);
            //获取融资产品详情
            ProductInfoVo productInfoVo = productDao.getProductInfo(id);
            //获取融资企业详情
            CompanyInfoVo companyInfoVo = financingDao.getCompanyInfo(id);
            //获取授信申请详情
            CreditVo creditVo = financingDao.getCreditVo(companyInfoVo.getId(),productInfoVo.getId());

            /**
             * 融资申请决策书对象封装
             */
            decisionSubmissionVo.setCustomerCompanyName(companyInfoVo.getCompanyName());//卖方企业名称
            decisionSubmissionVo.setCreditNo(companyInfoVo.getCreditNo());//统一社会信用代码
            decisionSubmissionVo.setBusinessType(productInfoVo.getBusinessTypeName());//产品业务类型

            decisionSubmissionVo.setCreditCode(creditVo.getCode());//授信编号
            decisionSubmissionVo.setCreditLine(creditVo.getCreditLine());//授信额度
            decisionSubmissionVo.setCreditEndTime(creditVo.getCreditEndTime());//授信到期日
            decisionSubmissionVo.setProductName(productInfoVo.getName());//融资产品

            Map<String,Object> params = new HashMap<>();
                params.put("id",id);//融资申请id
                params.put("customerCompanyId",companyInfoVo.getId());//融资企业id
                params.put("fundingCompanyId",productInfoVo.getCompanyId());//资金机构id
                params.put("productId",productInfoVo.getId());//产品id


                /**
                 * 授信可用额度  =  授信额度 -  融资余额 - 在途的融资额度
                 */
            // ProductCredits creditStatus = financingStartDao.getCreditStatus(params);
            // decisionSubmissionVo.setAvailableCreditLine(creditStatus.getAvailableCreditLine());//剩余可用额度（元）
            decisionSubmissionVo.setAvailableCreditLine(12.00);

            /**
             * 如果平台服务费是0 则，平台服务费 = 产品平台服务费率 * 融资金额
             */
            if(decisionSubmissionVo.getPlatformService() == 0.00 ){
                double platformService = decisionSubmissionVo.getAmountFinancing() * productInfoVo.getPscRate()/100;
                decisionSubmissionVo.setPlatformService(platformService);//平台服务费（元）
            }

            /**
             * 如果产品使用平台账户则，审批是自动显示企业开通的账号信息
             */
            if(productInfoVo.getIsPlatformAccount()){
                decisionSubmissionVo.setCompanyAccount(companyInfoVo.getCompanyAccount());//融资收款账户
                decisionSubmissionVo.setAccountName(companyInfoVo.getAccountName());//收款账户名称
                decisionSubmissionVo.setBankBranch(companyInfoVo.getBankBranch());//开户行网点名称
                decisionSubmissionVo.setBankNum(companyInfoVo.getBankNum());//开户行行号
            }

            List<DSFinancings> dsFinancingsList = financingDao.getDsFinancingsList(params);
            List<DSOtherFinancings> dsOtherFinancingsList  = financingDao.getDsOtherFinancingsList(id);
            List<DSPledgeBill> dsPledgeBillList = financingDao.getDsPledgeBillList(id);
                decisionSubmissionVo.setDsFinancingsList(dsFinancingsList);
                decisionSubmissionVo.setDsOtherFinancingsList(dsOtherFinancingsList);
                decisionSubmissionVo.setDsPledgeBillList(dsPledgeBillList);

            return Result.succeed(decisionSubmissionVo,"成功");
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional
    public Result audit(Financing financing) throws ServiceException {
        try {
            financing.setHandleStatus(1); //通过
            Result result = methodAuth(financing);//判断操作员和操作状态是否符合
            Map<String,Object> map = new HashMap<>();
            if(CodeEnum.SUCCESS.getCode() == result.getResp_code()){
                map = (Map<String,Object>)result.getData();
            }else{
                return result;
            }

            int status = financingDao.getFinancingStatus(financing.getId());//获取该笔融资状态
            financing.setStatus(status + 1); //处理操作后融资申请状态

            financingDao.addLog(map);//保存融资申请日志
            financingDao.addAuditInfo(financing);//保存审批详情

            financingDao.deleteOtherPlatfrom(financing.getId());//删除融资申请平台外融资情况
            financingDao.deletePledgeBill(financing.getId());//质押票据列表

            financingDao.saveOtherPlatfrom(financing);//保存融资申请平台外融资情况
            financingDao.savePledgeBill(financing);//保存质押票据列表

            /**
             * 审批通过时生成相关合同
             * */

          /* if(status ==2){
                contractTemplateService.generateFinancingProjectContract(financing);
            }
            contractTemplateService.generateFinancingProjectContract(financing);*/

            //付款单生成
            PaymentNoteVo paymentNoteVo = packagePaymentNoteVo(financing.getId());
            financingDao.savePaymentNote(paymentNoteVo);

            return Result.succeed("成功");
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Result reject(Financing financing) throws ServiceException {
        try {
            financing.setHandleStatus(2); //驳回
            Result result = methodAuth(financing);//判断操作员和操作状态是否符合
            Map<String,Object> map = new HashMap<>();
            if(CodeEnum.SUCCESS.getCode() == result.getResp_code()){
                map = (Map<String,Object>)result.getData();
            }else{
                return result;
            }

            int status = financingDao.getFinancingStatus(financing.getId());//获取该笔融资状态
            int i = (status == 0) ? 7 : (status - 1);//status = 7 作废
            financing.setStatus(i);
            financingDao.addLog(map);//保存融资申请日志
            financingDao.addAuditInfo(financing);//修改审批详情
            return Result.succeed("成功");
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Result ht() throws ServiceException {
        Financing financing = new Financing();
        financing.setId(1);
        contractTemplateService.generateFinancingProjectContract(1);
        return null;
    }

    public Result methodAuth(Financing financing){
        LoginAppUser loginUser = SysUserUtil.getLoginAppUser();
        if(loginUser == null)
            return Result.failed("获取当前用户登陆信息失败");
        Long userId = loginUser.getId();
        String companyId = loginUser.getCompanyId();//用户企业id
        Set<SysRole> roles = loginUser.getSysRoles();
        List<SysRole> list = new ArrayList(roles);
        String roleCode = list.get(0).getCode();//用户角色code，系统用户只有一个角色

        /**
         * 1，获取该笔融资状态
         *      status = 0，待初审；1，待审核；2，待审批
         *      以上状态融资信息可以审批
         *
         * 2，获取企业工作流（目前系统设计只有融资审批使用工作流）
         *      if( 1 级审批) --> 通过 （status = 3 生效） ；未通过 （status = 7 作废）
         *      if( 2 级审批)
         *          初审 --> 通过 （status = 1 待审核） ；未通过 （status = 7 作废）
         *          审核 --> 通过 （status = 3 生效）   ；未通过 （status = 0 待初审）
         *      if( 3 级审批)
         *          初审 --> 通过 （status = 1 待审核） ；未通过 （status = 7 作废）
         *          审核 --> 通过 （status = 2 待审批） ；未通过 （status = 0 待初审）
         *          审批 --> 通过 （status = 3 生效）   ；未通过 （status = 1 待审核）
         */
        int handleStatus = financing.getHandleStatus();//操作员操作状态（页面传输）（1，通过； 2，未通过）
        int status = financingDao.getFinancingStatus(financing.getId());//获取该笔融资状态
        WorkflowInfoVo workflowInfoVo = financingDao.getWorkflowInfoVo(companyId);//获取工作流
        if(workflowInfoVo == null){
            return Result.failedWith(null,-1000,"没有对应的工作流，请维护您的产品融资申请审批工作流");
        }
        List<CompanyWorkflow> rolesList = JSONObject.parseArray(workflowInfoVo.getFlowInfo(), CompanyWorkflow.class);//工作流-操作角色信息及操作内容信息列表
        if(rolesList.isEmpty()){
            return Result.failedWith(null,-1001,"工作流维护有误");
        }
        //status = 0,1,2 融资申请才能进行审批操作
        if(status != 0 && status != 1 && status != 2){
            return Result.failedWith(null,-1002,"不能审核");
        }

        //从工作流信息获取几级审批
        int auditTimes = rolesList.size();//几级审批

        //从工作流和融资申请信息状态获取该条记录操作的角色code
        String shouldRoleCode = "";
        for(CompanyWorkflow companyWorkflow : rolesList){
            if(status == companyWorkflow.getStatus()){
                shouldRoleCode = companyWorkflow.getCode();
            }
        }
        if(!roleCode.equals(shouldRoleCode)){
            return Result.failedWith(null,-1003,"操作员不能审批该条融资申请");
        }else{
            Map<String,Object> map = new HashMap<>();
            map.put("financingId",financing.getId());
            map.put("auditOpinion",financing.getAuditOpinion());
            map.put("userId",userId);
            map.put("status",financing.getHandleStatus());
            return Result.succeed(map,"success");
        }
    }

    public PaymentNoteVo packagePaymentNoteVo(Integer financingId){
        String userId = financingDao.getAdminName(financingId);
        SysUser sysUser = userFeignClient.findByUserId(userId);

        FinancingInfoVo financingInfo = financingDao.getFinancingInfo(financingId);

        Integer financingTimes = financingDao.getFinancingTimes(financingId);

        CompanyInfoVo companyInfoVo = financingDao.getCompanyInfo(financingId);

        PaymentNoteVo paymentNoteVo  = new PaymentNoteVo();
        paymentNoteVo.setPaymentDepartment("鄂尔多斯商业保理（天津）有限公司-业务部");
        paymentNoteVo.setAdminName(sysUser.getRealname());//系统操作员（融资初审岗人员-真实姓名）
        paymentNoteVo.setFinancingCode(financingInfo.getCode());
        paymentNoteVo.setPlanPayDate(financingInfo.getPlanInterestTime()); //计划付款日期（放款日期）
        //paymentNoteVo.setPlanAmount();//实际付款金额
        //paymentNoteVo.setFactoringContractCode();//商业保理合同编号
        paymentNoteVo.setFinancingTimes(financingTimes);//融资次数（同一个授信下的审批通过的融资次数）
        //paymentNoteVo.setreceivableContractCode();//应收账款转让确认书编号
        paymentNoteVo.setAmountFinancing(financingInfo.getAmountFinancing());//保理融资款（融资金额）
        paymentNoteVo.setFactoringServiceRate(financingInfo.getFactoringServiceRate()); //保理服务费率
        paymentNoteVo.setFactoringService(financingInfo.getFactoringService());//factoring_service
        paymentNoteVo.setFinancingRate(financingInfo.getFinancingRate());//融资利率
        BigDecimal financingRateAmount = CalculatingMoney.financingRateAmount(financingInfo.getAmountFinancing(),new BigDecimal(financingInfo.getFinancingRate()));
        paymentNoteVo.setFinancingRateAmount(financingRateAmount);//融资利息
        paymentNoteVo.setBuyBond(financingInfo.getBuyBond());//回购保证金
        paymentNoteVo.setLoanMoney(financingInfo.getLoanMoney());//实际付款金额
        paymentNoteVo.setCompanyName(companyInfoVo.getCompanyName());//收款单位名称
        paymentNoteVo.setBankName(financingInfo.getBankBranch());//开户行网点名称
        paymentNoteVo.setBankAccount(financingInfo.getBankNum());//银行账号
        //paymentNoteVo.setRemarks();//备注
        paymentNoteVo.setBusinessOperator(sysUser.getRealname());//业务经办人
        paymentNoteVo.setBusinessManager("");//业务部经理
        paymentNoteVo.setGeneralManager("");//总经理
        paymentNoteVo.setBusinessVicePresident("");//业务分管副总
        paymentNoteVo.setFinanceManager("");//财务部经理
        paymentNoteVo.setRiskManager("");//风控运营部经理
        paymentNoteVo.setFinancialReview("");//财务复核
        paymentNoteVo.setFundManagementPost("");//资金管理岗

        return paymentNoteVo;
    }

}
