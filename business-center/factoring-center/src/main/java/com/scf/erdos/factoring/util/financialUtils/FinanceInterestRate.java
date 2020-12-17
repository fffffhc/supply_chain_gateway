package com.scf.erdos.factoring.util.financialUtils;

import java.math.BigDecimal;

/**
 * @Description : 利率计算
 * @author：bao-clm
 * @date: 2020/10/14
 * @version：1.0
 */

public class FinanceInterestRate {

    /**
     *  年化利率计算
     *  利率为整数比如： 10 代表 10%
     *  融资利率 （年化率 - >利率/100*360）
     */
    public static BigDecimal year(Integer financingRate){
        if(financingRate == null){
            return new BigDecimal(0.00);
        }

        BigDecimal bigDecimal1 = new BigDecimal(financingRate);
        BigDecimal bigDecimal2 = new BigDecimal(100 * 360);

        /**
         * 小数点后留4位 - > scale = 4
         * 四舍五入 - > BigDecimal.ROUND_HALF_UP
         */
        BigDecimal bigDecimal3 = bigDecimal1.divide(bigDecimal2, 4, BigDecimal.ROUND_HALF_UP);
        return bigDecimal3;
    }

}
