package com.scf.erdos.factoring.service.impl.repaymentPlan;

import com.scf.erdos.factoring.model.repay.PayPlan;
import com.scf.erdos.factoring.util.financialUtils.FinanceInterestRate;
import com.scf.erdos.factoring.vo.financing.FinancingInfoVo;
import com.scf.erdos.factoring.vo.product.ProductInfoVo;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description : 还款周期2个月
 * @author：bao-clm
 * @date: 2020/10/14
 * @version：1.0
 */

@SuppressWarnings("all")
public class generate2Month {
    public static List<PayPlan> excute(ProductInfoVo productInfoVo, FinancingInfoVo financingInfoVo) throws ParseException {
        List<PayPlan> list = new ArrayList<>();
        Integer firstPayType = productInfoVo.getFirstPayType();//首次换息方式（1，当月；2，次月）
        Integer interestPayDay = productInfoVo.getInterestPayDay();//还息日（每月）

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String financingStartTime = now.format(formatter);//融资起息日（放款日，放款当前日）
        String financingEndTime = financingInfoVo.getFinancingEndTime(); //融资到期日
        List<String> monthList = getAllMonths.months(financingStartTime, financingEndTime, interestPayDay);//返回每月换息日

        Integer financingDay = Integer.valueOf(financingEndTime.split("-")[2]);//融资 日
        Integer financingStartDay = Integer.valueOf(financingStartTime.split("-")[2]);//融资起息 日
        Integer financingEndDay = Integer.valueOf(financingEndTime.split("-")[2]);//融资到期 日

        BigDecimal amountFinancing = financingInfoVo.getAmountFinancing();//融资金额
        BigDecimal financingRate =  FinanceInterestRate.year(financingInfoVo.getFinancingRate());//年化利率换算
        BigDecimal interestRepayment = new BigDecimal(0.00);//每期利息

        String financingCode = financingInfoVo.getCode();//融资编号

        //次月
        if (firstPayType == 2) {
            if (financingEndDay < interestPayDay || financingEndDay == interestPayDay) {
                Integer days = getAllDays.days(financingStartTime, financingEndTime);//计算利息日
                PayPlan payPlan = packageModel.getPayPlan(financingCode, 1, financingEndTime, amountFinancing, financingRate, days,true);
                list.add(payPlan);
            }

            if (financingEndDay > interestPayDay) {
                Integer days = getAllDays.days(financingStartTime, monthList.get(1));//计算利息日
                PayPlan payPlan = packageModel.getPayPlan(financingCode, 1, monthList.get(1), amountFinancing, financingRate, days,false);
                list.add(payPlan);

                Integer days1 = getAllDays.days(monthList.get(1), financingEndTime);//计算利息日
                PayPlan payPlan1 = packageModel.getPayPlan(financingCode, 2, financingEndTime, amountFinancing, financingRate, days1,true);
                list.add(payPlan1);
            }
        }

        //当月
        if (firstPayType == 1) {
            /**
             * 一 ， 放款日 小于 换息日 或者 等于 换息日
             */
            if (financingStartDay < interestPayDay || financingStartDay == interestPayDay) {
                Integer days = (interestPayDay - financingStartDay) == 0 ? 1:interestPayDay - financingStartDay;//计算利息日（当天换算一天利息）
                PayPlan payPlan = packageModel.getPayPlan(financingCode, 1, monthList.get(0), amountFinancing, financingRate, days,false);
                list.add(payPlan);

                if (financingEndDay < interestPayDay || financingEndDay == interestPayDay) {
                    Integer days1 = getAllDays.days(monthList.get(0), financingEndTime);//计算利息日
                    PayPlan payPlan1 = packageModel.getPayPlan(financingCode, 2, financingEndTime, amountFinancing, financingRate, days1,true);
                    list.add(payPlan1);
                }

                if (financingEndDay > interestPayDay) {
                    Integer days2 = getAllDays.days(monthList.get(0), monthList.get(1));//计算利息日
                    PayPlan payPlan2 = packageModel.getPayPlan(financingCode, 2, monthList.get(1), amountFinancing, financingRate, days2,false);
                    list.add(payPlan2);

                    Integer days3 = getAllDays.days(monthList.get(1), financingEndTime);//计算利息日
                    PayPlan payPlan3 = packageModel.getPayPlan(financingCode, 3, financingEndTime, amountFinancing, financingRate, days3,true);
                    list.add(payPlan3);
                }
            }


            /**
             * 二 ， 放款日 大于
             */
            if (financingStartDay > interestPayDay) {
                if (financingEndDay < interestPayDay || financingEndDay == interestPayDay) {
                    Integer days = getAllDays.days(financingStartTime, financingEndTime);//计算利息日
                    PayPlan payPlan = packageModel.getPayPlan(financingCode, 1, financingEndTime, amountFinancing, financingRate, days,true);
                    list.add(payPlan);
                }

                if (financingEndDay > interestPayDay) {
                    Integer days = getAllDays.days(financingStartTime, monthList.get(1));//计算利息日
                    PayPlan payPlan = packageModel.getPayPlan(financingCode, 1, monthList.get(1), amountFinancing, financingRate, days,false);
                    list.add(payPlan);

                    Integer days1 = getAllDays.days(monthList.get(1), financingEndTime);//计算利息日
                    PayPlan payPlan1 = packageModel.getPayPlan(financingCode, 2, financingEndTime, amountFinancing, financingRate, days1,true);
                    list.add(payPlan1);
                }
            }
        }
        return list;
    }
}
