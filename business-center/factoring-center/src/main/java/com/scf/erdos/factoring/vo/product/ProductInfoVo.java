package com.scf.erdos.factoring.vo.product;

import lombok.Data;
import java.io.Serializable;

/**
 * @Description : 产品详情视图
 * @author：bao-clm
 * @date: 2020/5/25
 * @version：1.0
 */
@Data
public class ProductInfoVo implements Serializable {

    private Integer id;
    private String name;//产品名称
    private Integer companyId;//资金机构id
    private String financingCompanyName;//资金机构名称
    private String businessType;//业务类型
    private String businessTypeName;//业务类型名称
    private Integer financingRate;//融资比例
    private String spType;//支持客户类型（多选已 & 隔开如： 1000&1001&1002）
    private String flowType;//融资方流程类型
    private Boolean isPlatformAccount;//是否平台账户（false，否；true，是）
    private String repayRemind;//还款到期提醒（多选已 & 隔开如： 1000&1001&1002）
    private Boolean isFapiaoReview;//是否发票验真（false，否；true，是）
    private Boolean chiFactoring;//是否池保理（false，否；true，是）
    private String isCredit;//授信流程（1，一级；2，二级；3，三级）
    private Boolean isPcertain;//是否按产品确定利率（false，否；true，是）
    private Integer isPcertainRate;//利率（%年化）
    private Integer prepayRate;//提前还款罚息日利率（%）
    private Integer prepayRateDay;//宽限日
    private Integer overDayRate;//逾期罚息日利率（%）
    private Integer firstPayType;//首次换息方式（1，当月；2，次月）
    private String payType;//还款方式
    private Integer interestPayDay;//换息日（每月）
    private String pscType;//平台服务费-方式
    private String pscPay;//平台服务费-付费方
    private Integer pscRate;//平台服务费-服务费率
    private String iscType;//信息服务费-方式
    private String iscPay;//信息服务费-付费方
    private Integer iscRate;//信息服务费-服务费率
    private String introduction;//产品说明
    private Boolean status;//状态
    private Boolean isDelete;//是否删除
    private String createTime;//创建时间

}
