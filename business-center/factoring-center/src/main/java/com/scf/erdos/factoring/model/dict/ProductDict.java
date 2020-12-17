package com.scf.erdos.factoring.model.dict;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description : 产品相关字典
 * @author：bao-clm
 * @date: 2020/5/25
 * @version：1.0
 */

@Data
public class ProductDict implements Serializable {

    private Integer id;//主键
    private String code;//code
    private String name;//名称
    private String type;//类型
    private int sort;//排序
    private boolean status;//有效状态

    public enum typeEnum {

        p001,//支持客户类型
        p002,//还款提醒
        p003,//付款方式
        p004,//担保方式
        p005,//服务费收费方式
        p006,//服务费付费方
        p007;//服务收费标准

    }
}
