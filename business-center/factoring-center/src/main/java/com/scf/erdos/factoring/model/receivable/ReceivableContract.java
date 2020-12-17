package com.scf.erdos.factoring.model.receivable;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description :  应收账款合同
 * @author：bao-clm
 * @date: 2020/5/12
 * @version：1.0
 */
@Data
public class ReceivableContract implements Serializable {
    /**
     * 主键
     */
    private String id;
    /**
     * 应收账款id
     */
    private String yszkId;
    /**
     * 合同编号
     */
    private String contractCode;
    /**
     * 合同名称
     */
    private String contractName;
    /**
     * 合同文件url
     */
    private String contractFile;
    /**
     * 合同日期
     */
    private String contractDate;
    /**
     * 商品/服务
     */
    private String goods;
    /**
     * 单价（元、吨）
     */
    private String unitPrice;
    /**
     * 买方企业
     */
    private String buyerCompany;
}
