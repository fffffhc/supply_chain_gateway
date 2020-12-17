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
 * @Description : 还款周期3个月或者以上
 * @author：bao-clm
 * @date: 2020/10/14
 * @version：1.0
 */
@SuppressWarnings("all")
public class generateNMonth {
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

        Integer times = monthList.size();
        //次月
        if (firstPayType == 2) {
            /**
             * 如果融资到期日大于 每月换息日 则最后一期还款（不包括在 monthList）
             */
            Integer lastPeriod = (financingEndDay > interestPayDay) ? times : times - 1;
            for(int i = 0;i < times;i++){
                if(i == 1){
                    Integer days = getAllDays.days(financingStartTime,monthList.get(i));//计算利息日
                    PayPlan payPlan = packageModel.getPayPlan(financingCode, 1, monthList.get(i), amountFinancing, financingRate, days,false);
                    list.add(payPlan);
                }else if(lastPeriod > i && i > 1){
                    Integer days = getAllDays.days(monthList.get(i - 1),monthList.get(i));//计算利息日
                    PayPlan payPlan = packageModel.getPayPlan(financingCode, i, monthList.get(i), amountFinancing, financingRate, days,false);
                    list.add(payPlan);
                }
            }
            /**
             * 最后一笔 本金利息一期换
             */
            Integer days = getAllDays.days(monthList.get(times-1),financingEndTime);//计算利息日
            PayPlan payPlan = packageModel.getPayPlan(financingCode, times, financingEndTime, amountFinancing, financingRate, days,true);
            list.add(payPlan);
        }

        //当月
        if (firstPayType == 1) {
            /**
             * 如果融资到期日大于 每月换息日 则最后一期还款（不包括在 monthList）
             */
            Integer lastPeriod = (financingEndDay > interestPayDay) ? times : times - 1;
            for(int i = 0;i < times;i++){
                if(financingStartDay < interestPayDay || financingStartDay == interestPayDay){
                    if(i == 0){
                        Integer days = getAllDays.days(financingStartTime,monthList.get(i));//计算利息日
                        Integer days1 = days == 0 ? 1 : days;
                        PayPlan payPlan = packageModel.getPayPlan(financingCode, 1, monthList.get(i), amountFinancing, financingRate, days1,false);
                        list.add(payPlan);
                    }else if(lastPeriod > i){
                        Integer days = getAllDays.days(monthList.get(i - 1),monthList.get(i));//计算利息日
                        PayPlan payPlan = packageModel.getPayPlan(financingCode, i, monthList.get(i), amountFinancing, financingRate, days,false);
                        list.add(payPlan);
                    }
                }

                if(financingStartDay > interestPayDay){
                    if(i == 1){
                        Integer days = getAllDays.days(financingStartTime,monthList.get(i));//计算利息日
                        PayPlan payPlan = packageModel.getPayPlan(financingCode, 1, monthList.get(i), amountFinancing, financingRate, days,false);
                        list.add(payPlan);
                    }else if(lastPeriod > i && i > 1){
                        Integer days = getAllDays.days(monthList.get(i - 1),monthList.get(i));//计算利息日
                        PayPlan payPlan = packageModel.getPayPlan(financingCode, i, monthList.get(i), amountFinancing, financingRate, days,false);
                        list.add(payPlan);
                    }
                }
            }
            /**
             * 最后一笔 本金利息一期换
             */
            Integer days = getAllDays.days(monthList.get(times-1),financingEndTime);//计算利息日
            PayPlan payPlan = packageModel.getPayPlan(financingCode, times, financingEndTime, amountFinancing, financingRate, days,true);
            list.add(payPlan);
        }

        return list;
    }

}
