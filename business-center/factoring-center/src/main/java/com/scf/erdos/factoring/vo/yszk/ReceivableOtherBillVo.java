package com.scf.erdos.factoring.vo.yszk;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description : 应收账款其它单据 视图实体类
 * @author：bao-clm
 * @date: 2020/5/12
 * @version：1.0
 */

@Data
public class ReceivableOtherBillVo implements Serializable {
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
    private Double unitPrice;
    /**
     * 净量（吨）
     */
    private String netWeight;
    /**
     * 金额
     */
    private Double amount;
    /**
     * 买方企业
     */
    private String buyerCompany;
}
