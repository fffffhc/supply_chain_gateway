package com.scf.erdos.factoring.vo.credit;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description : 定向授信页面 参数视图
 * @author：bao-clm
 * @date: 2020/6/8
 * @version：1.0
 */

@Data
public class CreditVo implements Serializable {

    private Integer id;//id
    private String fundingCompanyId;//授信机构id
    private String fundingCompanyName;//授信机构
    private String code;//授信编号
    private Integer businessScale;//经营规模（1，小型；2，中型；3，大型）
    private Integer productId;//产品id
    private Integer isCredit;//授信流程（1，一级；2，二级；3，三级）
    private String productName;//授信产品
    private Integer guaranteeType;//担保方式（1，无担保 ；2，最高额度担保）
    private Double creditLine;//授信额度
    private String creditStartTime;//授信起始日
    private String creditEndTime;//授信到期日
    private String customerLevel;//客户等级
    private Integer customerCompanyId;//企业id
    private String customerCompany;//企业名称
    private String creditNo;//统一社会信用代码
    private String businessLic;//营业执照（影像）
    private String legalIdFront;//法人身份证正面
    private String legalIdOther;//法人身份证反面
    private String registeredCapital;//注册资金
    private String registeredYears;//注册年限
    private String industryTypeName;//行业类型名称
    private String totalPreYear;//上一年业务总量
    private Boolean isDispute;//近一年是否有质量纠纷（false，否；true，是）
    private Boolean isSupply;//近6个月是否持续供货（false，否；true，是）
    private Integer buyerRate;//买方付款比例
    private String remitFrequency;//回款频率
    private Integer status;//状态（0，待初审；1，待审核；2，待审批；3，生效；4，作废；5，已到期）
    private String otherFile;//其它资料
    private List<ProductCreditLog> productCreditLogList;

}
