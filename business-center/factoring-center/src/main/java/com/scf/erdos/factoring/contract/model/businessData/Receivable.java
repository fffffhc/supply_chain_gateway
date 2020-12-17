package com.scf.erdos.factoring.contract.model.businessData;

import com.scf.erdos.factoring.util.financialUtils.NumberToCN;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Description : 应收账款信息
 * @author：bao-clm
 * @date: 2020/8/17
 * @version：1.0
 */

@Data
public class Receivable implements Serializable {
    /**
     * 主键
     */
    private int id;
    /**
     * 待转让资产编号
     */
    private String code;
    /**
     * 买方名称
     */
    private String buyerCompany;
    /**
     * 应收账款总金额
     */
    private BigDecimal yszkAmount;
    private String yszkAmountToCN;//应收账款总金额大写
    public String getYszkAmountToCN() {
        if(yszkAmount == null){
            yszkAmount = new BigDecimal(0.00);
        }
        return NumberToCN.number2CNMontrayUnit(yszkAmount);
    }
    /**
     * 发票总金额
     */
    private BigDecimal fapiaoAmount;
    /**
     * 其它单据总金额
     */
    private BigDecimal billAmount;
}
