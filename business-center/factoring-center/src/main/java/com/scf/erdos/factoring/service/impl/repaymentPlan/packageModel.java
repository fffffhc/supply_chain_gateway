package com.scf.erdos.factoring.service.impl.repaymentPlan;

import com.scf.erdos.factoring.model.repay.PayPlan;
import com.scf.erdos.factoring.util.financialUtils.CalculatingMoney;

import java.math.BigDecimal;

/**
 * @Description : 对象封装
 * @author：bao-clm
 * @date: 2020/10/14
 * @version：1.0
 */
public class packageModel {
    /**
     * PayPlan 对象封装
     */
    public static PayPlan getPayPlan(String financingCode, Integer period, String payDate,
                                     BigDecimal amountFinancing, BigDecimal financingRate, Integer days,Boolean endStatus) {
        BigDecimal interestRepayment = CalculatingMoney.financingInterest(amountFinancing,financingRate,days);//利息
        PayPlan payPlan = new PayPlan();
        payPlan.setFinancingCode(financingCode);//融资编号
        payPlan.setPeriod(period);//还款期次
        payPlan.setPayDate(payDate);//还款日期
        //换本金
        if(endStatus) {
            payPlan.setTotalRepayment(interestRepayment.add(amountFinancing));//应还款合计（元）
            payPlan.setPrincipalRepayment(amountFinancing);//应还本金
        }else{
            payPlan.setTotalRepayment(interestRepayment);//应还款合计（元）
            payPlan.setPrincipalRepayment(new BigDecimal(0.00));//应还本金
        }

        payPlan.setInterestRepayment(interestRepayment);//应还利息（元）
        return payPlan;
    }
}
