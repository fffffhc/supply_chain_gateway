package com.scf.erdos.factoring.model.receivable;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description : 应收账款-发票
 * @author：bao-clm
 * @date: 2020/5/13
 * @version：1.0
 */
@Data
public class ReceivableFapiao implements Serializable {
    /**
     * 主键
     */
    private String id;
    /**
     * 应收账款id
     */
    private String yszkId;
    /**
     * 发票号码
     */
    private String fpNo;
    /**
     * 发票代码
     */
    private String fpCode;
    /**
     * 发票日期
     */
    private String fpDate;
    /**
     * 金额
     */
    private BigDecimal fpAmount;
    /**
     * 买方企业
     */
    private String buyerCompany;
}
