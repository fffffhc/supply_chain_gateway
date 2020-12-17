package com.scf.erdos.factoring.vo.product;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description : 合同占位符
 * @author：bao-clm
 * @date: 2020/7/23
 * @version：1.0
 */

@Data
public class ProductContractPlaceholder implements Serializable {
    private Integer id;
    private String placeholder;
    private String placeholderName;
    private Integer type;
    private Integer sort;
}
