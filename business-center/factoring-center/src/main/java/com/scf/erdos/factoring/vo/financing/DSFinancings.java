package com.scf.erdos.factoring.vo.financing;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description : 客户在平台内融资情况
 * @author：bao-clm
 * @date: 2020/8/4
 * @version：1.0
 */

@Data
public class DSFinancings implements Serializable {

    private Integer id;//融资申请id
    private String code;//融资编号
    private String productName;//产品名称
    private double balance;//融资余额
    private String financingEndTime;//融资到期日
}
