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
 * @Description : 当月还款
 * @author：bao-clm
 * @date: 2020/10/14
 * @version：1.0
 */

@SuppressWarnings("all")
public class generate1Month {
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
            Integer days = getAllDays.days(financingStartTime,financingEndTime);//计算利息日
            PayPlan payPlan = packageModel.getPayPlan(financingCode, 1, financingEndTime, amountFinancing, financingRate, days,true);
            list.add(payPlan);
        }
        //当月
        if (firstPayType == 1) {
            if ((financingStartDay < interestPayDay || financingStartDay == interestPayDay) && interestPayDay < financingDay) {
                Integer days1 = interestPayDay - financingStartDay;//计算利息日
                if (days1 == 0) {
                    days1 = days1 + 1; //当天换算一天利息
                }
                PayPlan payPlan1 = packageModel.getPayPlan(financingCode, 1, monthList.get(0), amountFinancing, financingRate, days1,false);
                list.add(payPlan1);

                Integer days2 = financingEndDay - interestPayDay;//计算利息日
                PayPlan payPlan2 = packageModel.getPayPlan(financingCode, 2, financingEndTime, amountFinancing, financingRate, days2,true);
                list.add(payPlan2);
            } else {
                Integer days = getAllDays.days(financingStartTime, financingEndTime);//计算利息日
                if (days == 0) {
                    days = days + 1; //当天换算一天利息
                }
                PayPlan payPlan = packageModel.getPayPlan(financingCode, 1, financingEndTime, amountFinancing, financingRate, days,true);
                list.add(payPlan);
            }
        }
        return list;
    }
}
