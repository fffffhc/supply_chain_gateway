package com.scf.erdos.factoring.model.repay;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description : 还款计划
 * @author：bao-clm
 * @date: 2020/10/13
 * @version：1.0
 */

@Data
public class PayPlan implements Serializable {

    private Integer id;
    private String financingCode;
    private Integer period;
    private String payDate;
    private BigDecimal totalRepayment;
    private BigDecimal principalRepayment;
    private BigDecimal interestRepayment;
    private Integer status;
    private String explain;
    private Date create_time;
}
