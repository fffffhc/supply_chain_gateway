package com.scf.erdos.factoring.model.financing;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description : 融资申请保存
 * @author：bao-clm
 * @date: 2020/8/26
 * @version：1.0
 */

@Data
public class FinancingSave implements Serializable {

    private String code;//融资编号
    private String productId;//产品id
    private Integer receivableId;//应收账款id
    private Double amountFinancing;//融资申请金额
    private Double amountTransfer;//转让总金额
    private String detailes;//明细文件
    private String rBuyerCompanyName;//应收账款买方企业
    private String buyerCompanyName;//买方企业名称（明保理时必填）
    private long userId;
    private String companyId;
    private Integer fundingCompanyId;

}
