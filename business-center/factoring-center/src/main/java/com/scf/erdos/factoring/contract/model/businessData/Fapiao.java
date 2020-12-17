package com.scf.erdos.factoring.contract.model.businessData;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description : 发票列表
 * @author：bao-clm
 * @date: 2020/8/13
 * @version：1.0
 */

@Data
public class Fapiao implements Serializable {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

    /**
     * 发票号码
     */
    private String fpNo;
    /**
     * 发票代码
     */
    private String fpCode;
    /**
     * 发票日期
     */
    private Date fpDate;
    public String getFpDate() {
        if(fpDate == null || "".equals(fpDate)){
            return null;
        }
        return sdf.format(fpDate);
    }
    /**
     * 金额
     */
    private BigDecimal fpAmount;
    /**
     * 买方企业
     */
    private String buyerCompany;
}
