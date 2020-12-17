package com.scf.erdos.factoring.model.credit;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description : 融资产品授信
 * @author：bao-clm
 * @date: 2020/6/9
 * @version：1.0
 */

@Data
public class ProductCredit implements Serializable {

    private Integer id;//id
    private String fundingCompanyId;//授信机构id
    private String code;//授信编号
    private String productName;//授信产品
    private String guaranteeType;//担保方式
    private Double creditLine;//授信额度
    private String creditStartTime;//授信起始日
    private String creditEndTime;//授信到期日
    private String customerLevel;//客户等级
    private Integer customerCompanyId;//企业id
    private String totalPreYear;//上一年业务总量
    private Boolean isDispute;//近一年是否有质量纠纷（false，否；true，是）
    private Boolean isSupply;//近6个月是否持续供货（false，否；true，是）
    private Integer buyerRate;//买方付款比例
    private String remitFrequency;//回款频率
    private String status;//操作员操作状态（1，同意； 2，驳回）
    private String creditStatus;//状态（0，待初审；1，待审核；2，待审批；3，生效；4，作废；5，已到期）
    private String creditOpinion;//授信意见
    private Long userId;//审核人id
    private String otherFile;//其它资料
}
