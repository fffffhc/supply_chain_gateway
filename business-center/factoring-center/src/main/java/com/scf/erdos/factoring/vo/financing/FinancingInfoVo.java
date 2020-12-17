package com.scf.erdos.factoring.vo.financing;

import com.scf.erdos.factoring.vo.yszk.ReceivableContractVo;
import com.scf.erdos.factoring.vo.yszk.ReceivableFapiaoVo;
import com.scf.erdos.factoring.vo.yszk.ReceivableOtherBillVo;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Description : 融资申请详情
 * @author：bao-clm
 * @date: 2020/8/3
 * @version：1.0
 */

@Data
public class FinancingInfoVo implements Serializable {
    private Integer id;//融资申请id
    private String code;//融资申请编号
    private String financingCompanyName;//资金机构
    private Integer productId;//产品id
    private String productName;//融资产品名称
    private Integer financingRate;//融资比例
    private String payType;//还款方式
    private Integer interestPayDay;//还息日
    private Integer financingInterest;//融资利率
    private Integer factoringServiceRate;//保理服务费率
    private String tag;//资产标签
    private String receivableCode;//待转让资产编号
    private String financingEndTime;//融资到期日
    private double yszkAmount;//应收账款金额
    private double fapiaoAmount;//发票金额合计
    private String planInterestTime;//计划起息日（计划付款日期）
    private double billAmount;//其他单据金额合计
    private BigDecimal buyBond;//回购保证金（元）
    private Integer buyerCompanyId;//买方企业id
    private String buyerCompanyName;//买方企业名称
    private double transferFinancing;//转让金额
    private BigDecimal amountFinancing;//融资申请金额
    private BigDecimal factoringService;//保理服务费
    private BigDecimal LoanMoney;//实际放款金额
    private double platformService;//平台服务费
    private String detailes;//其它明细
    private Integer status;//融资申请状态
    private Integer receivableId;//应收账款id

    private String bankBranch;//开户行网点名称
    private String bankNum;//开户行行号

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
