package com.scf.erdos.factoring.vo.pay;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 付款单
 * @version 1.0
 * @date 2020/9/8 14:23
 */

@Data
public class PaymentNoteVo {
    private Integer id;
	private String code;
	private String paymentDepartment;
    private String adminName;
    private String financingCode;
    private String planPayDate;
    private String planAmount;
    private String factoringContractCode;
    private Integer financingTimes;
    private String receivableContractCode;
    private BigDecimal amountFinancing;
    private Integer factoringServiceRate;
    private BigDecimal factoringService;
    private Integer financingRate;
    private BigDecimal financingRateAmount;
    private BigDecimal buyBond;
    private BigDecimal loanMoney;
    private String companyName;
    private String bankName;
    private String bankAccount;
    private String remarks;
    private String businessOperator;
    private String businessManager;
    private String generalManager;
    private String businessVicePresident;
    private String financeManager;
    private String riskManager;
	private String financialReview;
    private String fundManagementPost;
	private String createTime;

}
