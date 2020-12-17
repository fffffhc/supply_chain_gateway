package com.scf.erdos.factoring.model.receivable;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description : 应收账款其它单据
 * @author：bao-clm
 * @date: 2020/5/12
 * @version：1.0
 */

@Data
public class ReceivableOtherBill implements Serializable {
    /**
     * 主键
     */
    private String id;
    /**
     * 应收账款id
     */
    private String yszkId;
    /**
     * 单据号
     */
    private String billCode;
    /**
     * 单据类型
     */
    private String billType;
    /**
     * 单据日期
     */
    private String billDate;
    /**
     * 单价（元、吨）
     */
    private BigDecimal unitPrice;
    /**
     * 净量（吨）
     */
    private String netWeight;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 买方企业
     */
    private String buyerCompany;
}
