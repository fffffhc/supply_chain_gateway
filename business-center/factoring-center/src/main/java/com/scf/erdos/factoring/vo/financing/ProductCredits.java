package com.scf.erdos.factoring.vo.financing;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description : 授信额度情况
 * @author：bao-clm
 * @date: 2020/7/30
 * @version：1.0
 */

@Data
public class ProductCredits implements Serializable {

    private Integer id;//授信申请id
    private String code;//授信编号
    private String productName;//授信产品名
    private String fundingCompanyName;//授信机构
    private double creditLine;//授信额度
    private double availableCreditLine;//授信可用额度
    private double toBeRepaid;//待还款融资
    private double onlineFinancing;//在途融资申请
    private String applyTime;//授信申请日
    private String creditEndTime;//授信到期日
    private String status;//授信状态状态（0，待初审；1，待审核；2，待审批；3，生效；4，作废；5，已到期）

}
