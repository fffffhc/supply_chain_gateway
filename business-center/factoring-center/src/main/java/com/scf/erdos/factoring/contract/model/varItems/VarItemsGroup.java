package com.scf.erdos.factoring.contract.model.varItems;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description : 合同模板占位符组
 * @author：bao-clm
 * @date: 2020/12/1
 * @version：1.0
 */

@Data
public class VarItemsGroup implements Serializable {

    public VarItemsGroup() {};

    public VarItemsGroup(List<VarItems> vars) {
        this.vars = vars;
    }

    /** 变量参数 */
    private List<VarItems> vars = new ArrayList<>();
}
