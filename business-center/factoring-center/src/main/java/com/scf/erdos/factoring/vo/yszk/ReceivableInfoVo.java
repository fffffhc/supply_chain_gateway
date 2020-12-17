package com.scf.erdos.factoring.vo.yszk;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description : 应收账款详情 视图
 * @author：bao-clm
 * @date: 2020/5/12
 * @version：1.0
 */

@Data
public class ReceivableInfoVo implements Serializable {

    /**
     * 主键
     */
    private String id;
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
    private Boolean status;
    /**
     * 合同列表
     */
    private List<ReceivableContractVo> contractList;
    /**
     * 发票列表
     */
    private List<ReceivableFapiaoVo> fapiaoList;
    /**
     * 其它单据列表
     */
    private List<ReceivableOtherBillVo> otherBillList;
}
