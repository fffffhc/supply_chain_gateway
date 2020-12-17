package com.scf.erdos.factoring.vo.product;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description : 产品分页列表视图
 * @author：bao-clm
 * @date: 2020/5/25
 * @version：1.0
 */

@Data
public class ProductPageVo implements Serializable {

    private Long id;//主键
    private String code;//产品编号
    private String name;//产品名
    private String companyName;//资金机构名
    private String contract;//合同（0，无电子合同；1，有电子合同）
    private boolean status;//状态
    private String createTime;//创建时间
}
