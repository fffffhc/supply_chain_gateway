package com.scf.erdos.factoring.vo.pay;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * @author FUHAOCHENG
 * @version 1.0
 * @date 2020/9/4 15:09
 */

@Data
public class PendingLondInformationVo implements Serializable {

    private Integer id;//融资id
    private Integer creditId;//授信编号
    private String code;//融资编号
    private Integer businessType;//业务标志
    private String buyerCompany;//买方名称
    private String companyName;//卖方企业名称
    private String productName;//产品名称
    private Double amountFinancing;//融资金额（元）
    private Double loanMoney;//实际放款（元）
    private Integer status;//状态（0，待初审；1，待审核；2，待审批；3，待签约；4，待放款；5，待还款；6，已结清；7，作废；8，已到期；9，已逾期）
    private Date applyTime;//申请时间
}
