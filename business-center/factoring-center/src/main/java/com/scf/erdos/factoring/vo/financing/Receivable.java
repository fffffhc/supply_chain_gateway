package com.scf.erdos.factoring.vo.financing;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description : 应收账款
 * @author：bao-clm
 * @date: 2020/7/31
 * @version：1.0
 */
@Data
public class Receivable implements Serializable {
    /**
     * 应收账款id
     */
    private String id;
    /**
     * 待转让资产编号
     */
    private String code;
    /**
     * 资产标签
     */
    private String tag;
}
