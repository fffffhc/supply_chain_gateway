package com.scf.erdos.factoring.model.Product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Description :融资产品主表实体类
 * @author：bao-clm
 * @date: 2020/5/22
 * @version：1.0
 */

@Data
@ApiModel(value = "ProductInfo实体类", description = "融资产品主表实体类")
public class ProductInfo implements Serializable {

	private Integer id;
	private String name;//产品名称
	private String code;//产品编号
	private String companyId;//资金机构id
	private Integer financingRate;//融资比例
	private String businessType;//业务类型
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
	private Long userId;//创建人id
	private Long updateUserId;//修改人id
	private String createTime;//创建时间
}
