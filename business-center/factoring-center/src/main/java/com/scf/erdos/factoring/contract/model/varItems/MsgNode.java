package com.scf.erdos.factoring.contract.model.varItems;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description : 占位符节点对象
 * @author：bao-clm
 * @date: 2020/8/13
 * @version：1.0
 */
public class MsgNode implements Serializable {

    @Data
    public static class VarGroup{
        private String name; //分组名称
        private List<VarItem> varItems = new ArrayList<>(); //变量参数
    }

    @Data
    public static class VarItem{
        private String key; //变量键
        private String value; //变量值
    }

}
