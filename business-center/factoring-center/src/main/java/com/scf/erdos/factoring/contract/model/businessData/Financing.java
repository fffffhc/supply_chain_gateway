package com.scf.erdos.factoring.contract.model.businessData;

import com.scf.erdos.factoring.util.financialUtils.NumberToCN;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description : 融资信息
 * @author：bao-clm
 * @date: 2020/12/2
 * @version：1.0
 */

@Data
@SuppressWarnings("all")
public class Financing implements Serializable {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

    private Integer id;//融资id
    private String financingContractCode;//融资合同编号
    private Date contractDate;//融资合同日期
    private String contractDateFormat;//融资合同日期
    public String getContractDate(String contractDate) {
        if(contractDate == null || "".equals(contractDate)){
            return null;
        }
        return sdf.format(contractDate);
    }

    private BigDecimal transferFinancing;//转让总金额小写
    private String transferFinancingToCN;//融资金额大写
    public String getTransferFinancingToCN() {
        return NumberToCN.number2CNMontrayUnit(transferFinancing);
    }

    private BigDecimal amountFinancing;//融资金额小写
    private String amountFinancingToCN;//融资金额大写
    public String getAmountFinancingToCN() {
        return NumberToCN.number2CNMontrayUnit(amountFinancing);
    }

    private Date auditedDate;//融资审批日（说明：审批通过当日）
    public String getAuditedDate() {
        if(auditedDate == null || "".equals(auditedDate)){
            return null;
        }
        return sdf.format(auditedDate);
    }

    private Date receivableTime;//应收账款到期日
    public String getReceivableTime(String receivableTime) {
        return sdf.format(receivableTime);
    }

    private BigDecimal factoringService;//保理服务费小写
    private String factoringServiceToCN;//保理服务费大写
    public String getFactoringServiceToCN(String factoringServiceToCN) {
        return NumberToCN.number2CNMontrayUnit(factoringService);
    }

    private String financingRate;//融资利率
    //private String //融资利息小写
    //private String //融资利息大写
    private String companyAccount;//收款账号
    private String accountName;//收款账户名
    private String bankBranch;//收款账户开户行名称
    //private String //融资批次（说明：同一授信项下第x笔融资，其中x为融资批次）
    private String financingEndTime;//融资到期日
    public String getFinancingEndTime(String financingEndTime) {
        return sdf.format(financingEndTime);
    }

    private String factoringServiceRate;//保理服务费率
    private Integer customerCompanyId;//融资企业id
    private Integer fundingCompanyId;//资金企业id
    private Integer creditId;//授信id
    private Integer productId;//产品id
    private Integer receivableId; //应收账款id
}
