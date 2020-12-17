package com.scf.erdos.factoring.vo.financing;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Description : 融资管理列表
 * @author：bao-clm
 * @date: 2020/7/29
 * @version：1.0
 */

@Data
public class FinancingPageVo implements Serializable {

    private Integer id;//融资申请id
    private String creditCode;//授信编号
    private String code;//融资编号
    private String buyerCompanyName;//买方名称
    private String productName;//融资产品
    private double amountFinancing;//融资金额
    private int financingRate;//融资利率
    private int factoringServiceRate;//保理服务费率
    private int businessType;//1，融资 ；2，置换
    private int status;//状态（0，待初审；1，待审核；2，待审批；3，待签约；4，待放款；5，待还款；6，已结清；7，作废；8，已到期；9，已逾期）
    private Boolean statusActive; //true 按钮展示 ， false 按钮不展示
    private String applyTime;//申请时间
    private String flowInfo;//工作流审批json 包
    private String companyLabel1;//企业标签（前端通过企业标签判断是否展示 “决策意见书”tag）

}
