package com.scf.erdos.factoring.vo.product;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description : 产品电子合同详情
 * @author：bao-clm
 * @date: 2020/7/27
 * @version：1.0
 */

@Data
public class ProductContractInfoVo implements Serializable {
    private Long id;//主键
    private String code;//产品编号
    private String productId;//产品id
    private String name;//合同名
    private String signingStage;//签署阶段
    private Boolean isOrderSigning;//是否按顺序签署（false，否；true，是）
    private String partyA;//甲方
    private String partyB;//乙方
    private String partyC;//丙方
    private String partyD;//丁方
    private String partyE;//戊方
    private String contractUrl;//合同模板url
    private String contractDescribe;
    private String createTime;//创建时间
    private Integer contractTemplateId;//合同模板di
}
