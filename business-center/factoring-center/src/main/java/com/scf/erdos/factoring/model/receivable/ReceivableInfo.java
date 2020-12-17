package com.scf.erdos.factoring.model.receivable;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description : 应收账款主表
 * @author：bao-clm
 * @date: 2020/5/13
 * @version：1.0
 */
@Data
public class ReceivableInfo implements Serializable {
    /**
     * 主键
     */
    private int id;
    /**
     * 待转让资产编号
     */
    private String code;
    /**
     * 买方名称
     */
    private String buyerCompany;
    /**
     * 应收账款总金额
     */
    private Double yszkAmount;
    /**
     * 发票总金额
     */
    private Double fapiaoAmount;
    /**
     * 其它单据总金额
     */
    private Double billAmount;
    /**
     * 资产标签
     */
    private String tag;
    /**
     * 企业id
     */
    private String companyId;
    /**
     * 录入人id
     */
    private Long userId;
    /**
     * 修改人id
     */
    private Long updateUserId;
    /**
     * 合同列表
     */
    private List<ReceivableContract> contractList;
    /**
     * 发票列表
     */
    private List<ReceivableFapiao> fapiaoList;
    /**
     * 其它单据列表
     */
    private List<ReceivableOtherBill> otherBillList;
}
