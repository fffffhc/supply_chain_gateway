package com.scf.erdos.factoring.vo.product;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description : 产品合同
 * @author：bao-clm
 * @date: 2020/7/23
 * @version：1.0
 */

@Data
public class ProductContractsVo implements Serializable {

    private String id;
    private String code;
    private String name;
    private String signingStage;
    private String contractUrl;
    private String productId;//产品id
    private Boolean isMasterContract;//是否主合同（0，否；1，是）
    private Boolean isOrderSigning;//是否按顺序签署（false，否；true，是）
    private String partyA;//甲方
    private String partyB;//乙方
    private String partyC;//丙方
    private String partyD;//丁方
    private String partyE;//戊方
    private String contractDescribe;
    private String createTime;//创建时间

}
