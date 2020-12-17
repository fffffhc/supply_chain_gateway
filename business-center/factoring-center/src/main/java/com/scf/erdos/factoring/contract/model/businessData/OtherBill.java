package com.scf.erdos.factoring.contract.model.businessData;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description : 其它票据列表
 * @author：bao-clm
 * @date: 2020/8/13
 * @version：1.0
 */

@Data
public class OtherBill implements Serializable {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

    private String billCode;//单据号
    private String billType;//单据类型
    private Date billDate;//单据日期
    public String getBillDate() {
        if(billDate == null || "".equals(billDate)){
            return null;
        }
        return sdf.format(billDate);
    }

    private Double unitPrice;//单价（元、吨）
    private String netWeight;//净量（吨）
    private BigDecimal amount;//金额
    private String buyerCompany;//买方企业
}
