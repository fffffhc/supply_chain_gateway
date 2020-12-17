package com.scf.erdos.factoring.vo.financing;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description : 融资申请决策意见书
 * @author：bao-clm
 * @date: 2020/8/4
 * @version：1.0
 */
@Data
public class DecisionSubmissionVo implements Serializable {

    private Integer id;//融资申请id

    private String customerCompanyName;//卖方企业名
    private String creditNo;//社会统一信用代码
    private String businessType;//产品业务类型
    private String customerLevel;//融资人等级

    private String creditCode;//授信编号
    private double creditLine;//授信额度
    private String creditEndTime;//授信到期日
    private String productName;//融资产品
    private double availableCreditLine;//剩余可用额度（元）

    private double amountFinancing;//本次保理融资额
    private String receivableTime;//应收账款到期日
    private String planInterestTime;//计划起息日
    private String financingEndTime;//融资到期日
    private Integer factoringServiceRate;//保理服务费率
    private Integer financingRate;//融资利率（%/年化）
    private double factoringService;//保理服务费（元）
    private double platformService;//平台服务费（元）
    private String overMoney;//逾期违约金（%/日）
    private Integer prepayRateDay;//宽限期（天）
    private double buyBond;//回购保证金（元）
    private double loanMoney;//实际放款金额（元）

    private String companyAccount;//融资收款账户
    private String accountName;//收款账户名称
    private String bankBranch;//开户行网点名称
    private String bankNum;//开户行行号

    private String checkFile;//调查核实资料

    private List<DSFinancings> dsFinancingsList;
    private List<DSOtherFinancings> dsOtherFinancingsList;
    private List<DSPledgeBill> dsPledgeBillList;
}
