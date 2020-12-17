package com.scf.erdos.factoring.service.impl.repaymentPlan;

import com.scf.erdos.factoring.model.repay.PayPlan;
import com.scf.erdos.factoring.vo.financing.FinancingInfoVo;
import com.scf.erdos.factoring.vo.product.ProductInfoVo;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description : 还款计划生成
 * @author：bao-clm
 * @date: 2020/10/14
 * @version：1.0
 */

@SuppressWarnings("all")
public class generateRepaymentPlan {

    public static List<PayPlan> generateMethod(ProductInfoVo productInfoVo,FinancingInfoVo financingInfoVo) throws ParseException{
        List<PayPlan> list = new ArrayList<>();
        Integer interestPayDay = productInfoVo.getInterestPayDay();//还息日（每月）
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String financingStartTime = now.format(formatter);//融资起息日（放款日，放款当前日）
        String financingEndTime = financingInfoVo.getFinancingEndTime(); //融资到期日
        List<String> monthList = getAllMonths.months(financingStartTime, financingEndTime, interestPayDay);//返回每月换息日

        Integer currentMonthPay = monthList.size();

        if (currentMonthPay == 1) {
            list = generate1Month.excute(productInfoVo,financingInfoVo);
        }else if(currentMonthPay == 2){
            list = generate2Month.excute(productInfoVo,financingInfoVo);
        }else if(currentMonthPay >2){
            list = generateNMonth.excute(productInfoVo,financingInfoVo);
        }
        return list;
    }

    //test
    public static void main(String[] args) throws ParseException {
        ProductInfoVo productInfoVo = new ProductInfoVo();
            productInfoVo.setInterestPayDay(15);//换息日
            productInfoVo.setFirstPayType(1);//首次换息方式（1，当月；2，次月）
        FinancingInfoVo financingInfoVo = new FinancingInfoVo();
            financingInfoVo.setAmountFinancing(new BigDecimal(10000.10).setScale(2, BigDecimal.ROUND_HALF_UP));//融资金额
            financingInfoVo.setFinancingEndTime("2021-02-21");//还款到期日
            financingInfoVo.setFinancingRate(12);//融资利率
            financingInfoVo.setCode("CX20201014");

        List<PayPlan> list = generateMethod(productInfoVo,financingInfoVo);
        System.out.println(list);
    }

}
