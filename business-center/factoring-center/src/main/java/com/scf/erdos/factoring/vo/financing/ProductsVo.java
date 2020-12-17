package com.scf.erdos.factoring.vo.financing;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description : 融资产品视图
 * @author：bao-clm
 * @date: 2020/6/8
 * @version：1.0
 */

@Data
public class ProductsVo implements Serializable {

    private String id;//产品id

    private String code;//授信编号

    private String productName;//产品名称

    private String financingCompany;//资金机构

    private String introduction;//产品介绍

    private String status;// 授信状态

}
