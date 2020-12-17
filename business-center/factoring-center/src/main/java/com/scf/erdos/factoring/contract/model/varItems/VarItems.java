package com.scf.erdos.factoring.contract.model.varItems;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description : 合同模板占位符 - json
 * @author：bao-clm
 * @date: 2020/12/1
 * @version：1.0
 */

@Data
public class VarItems implements Serializable {

    /** 唯一编号（分组号+组内代码） */
    private String sn;

    /** 组内代码 */
    private String code;

    /** 变量名称 */
    private String name;

    /** el表达式 */
    private String el;

    /** 序号 */
    private Integer seq;

    /** 功能 */
    private String fun;
}
