package com.scf.erdos.factoring.contract.model.businessData;

import lombok.Data;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @Description : 合同参数
 * @author：bao-clm
 * @date: 2020/9/15
 * @version：1.0
 */

@Data
public class ContractParams {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
    Calendar date = Calendar.getInstance();
    String year = String.valueOf(date.get(Calendar.YEAR)); //当前年份

    private String basicContractName; //基础合同名称
    private int basicContractSize; //基础合同件数
    private int receivableDetailSize; //应收账款明细件数（应收账款发票件数 + 其它票据件数）
    private int fapiaoSize;//发票件数
    private int otherBillSize;//其它票据件数

    private String financingAuditDate; //融资审批日
    public String getFinancingAuditDate() {
        return sdf.format(financingAuditDate);
    }

    private BigDecimal financingInterest; //融资利息

    private String contractAllCount; //系统-合同-总量
    private String contractCompanyCount; //企业-合同-总量
    private String head; //编号头部（资本运营保理 首字母大写）
    private String type; //类型（资本 F）
    private String productType; //产品类型
    private String contractCode; //合同编号
    public String getContractCode() {
        /**
         * 编号生成规则 ：
         * 编号头部（资本运营保理 首字母大写） + year + "/" + 类型（资本 F） +
         * "/" + 产品类型 + "【"  +  系统-合同-总量 + "】" + "【"  +  企业-合同-总量 + "】"
         *  ZYBL2020/F/YSZK-【33】-【3】
         *
         */
        StringBuilder sb = new StringBuilder();
        sb.append(head);
        sb.append(year);
        sb.append("/");
        sb.append(type);
        sb.append("/");
        sb.append(productType);
        sb.append("-【");
        sb.append(contractAllCount);
        sb.append("】-【");
        sb.append(contractCompanyCount);
        sb.append("】");
        return sb.toString();
    }

}
