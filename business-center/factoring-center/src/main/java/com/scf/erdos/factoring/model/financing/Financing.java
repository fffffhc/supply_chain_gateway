package com.scf.erdos.factoring.model.financing;

import com.scf.erdos.factoring.vo.financing.DSOtherFinancings;
import com.scf.erdos.factoring.vo.financing.DSPledgeBill;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * @Description : 融资申请
 * @author：bao-clm
 * @date: 2020/8/5
 * @version：1.0
 */

@Data
public class Financing implements Serializable {

    private Integer id;//融资申请id
    private Integer productId;
    private String customerLevel;//融资人等级
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
    private Integer handleStatus;//操作员操作状态（1，通过； 2，驳回）
    private String auditOpinion;//驳回原因
    private Integer status;//状态（0，待初审；1，待审核；2，待审批；3，待签约；4，待放款；5，待还款；6，已结清；7，作废；8，已到期；9，已逾期）

    private List<DSOtherFinancings> dsOtherFinancingsList;
    private List<DSPledgeBill> dsPledgeBillList;
}
