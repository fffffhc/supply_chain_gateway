package com.scf.erdos.factoring.vo.financing;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description : 客户在平台外融资情况
 * @author：bao-clm
 * @date: 2020/8/4
 * @version：1.0
 */
@Data
public class DSOtherFinancings implements Serializable {

    private Integer id;
    private String productName;//融资产品名称
    private double financingMoney;//累计融资金额
    private double balance;//融资余额
}
