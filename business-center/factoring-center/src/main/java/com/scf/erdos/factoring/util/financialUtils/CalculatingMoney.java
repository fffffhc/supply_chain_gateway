package com.scf.erdos.factoring.util.financialUtils;

import java.math.BigDecimal;

/**
 * @Description : 计算钱
 * @author：bao-clm
 * @date: 2020/10/15
 * @version：1.0
 */
public class CalculatingMoney {

    /**
     * 融资利息计算（按年计算 利率/360 * 实际天数）
     * 融资金额 ：amountFinancing
     * 融资利率：financingRate
     * 融资天数：days
     */
    public static BigDecimal financingInterest(BigDecimal amountFinancing,BigDecimal financingRate,Integer days){
        if(amountFinancing == null || financingRate == null || days == null){
            return new BigDecimal(0.00);
        }
        BigDecimal financingInterestMoney = amountFinancing.multiply(financingRate).multiply(new BigDecimal(days));
        /**
         * 小数点后保留俩位
         * 四舍五入
         */
        return financingInterestMoney.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 融资利息计算（按笔计算）
     * 融资金额 ：amountFinancing
     * 融资利率：financingRate
     * 融资天数：days
     */
    public static BigDecimal financingRateAmount(BigDecimal amountFinancing,BigDecimal financingRate){
        if(amountFinancing == null || financingRate == null){
            return new BigDecimal(0.00);
        }
        BigDecimal financingInterestMoney = amountFinancing.multiply(financingRate);
        /**
         * 小数点后保留俩位
         * 四舍五入
         */
        return financingInterestMoney.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
