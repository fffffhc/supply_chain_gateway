package com.scf.erdos.factoring.contract.util.table;

import com.alibaba.fastjson.JSONObject;
import com.scf.erdos.factoring.contract.model.ContractVo;
import com.scf.erdos.factoring.contract.model.varItems.VarItems;

public interface TableVarRender {

    String renderMode(JSONObject context, ContractVo contractVo, VarItems sysVarItem);
}
