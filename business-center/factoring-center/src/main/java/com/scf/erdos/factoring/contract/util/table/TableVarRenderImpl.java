package com.scf.erdos.factoring.contract.util.table;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scf.erdos.factoring.contract.model.ContractVo;
import com.scf.erdos.factoring.contract.model.businessData.BaseContract;
import com.scf.erdos.factoring.contract.model.businessData.Fapiao;
import com.scf.erdos.factoring.contract.model.businessData.OtherBill;
import com.scf.erdos.factoring.contract.model.varItems.VarItems;
import com.scf.erdos.factoring.model.receivable.ReceivableContract;
import com.scf.erdos.factoring.model.receivable.ReceivableFapiao;
import com.scf.erdos.factoring.model.receivable.ReceivableOtherBill;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description : 表格数据处理
 * @author：bao-clm
 * @date: 2020/9/11
 * @version：1.0
 */

@Component
@Slf4j
@SuppressWarnings("all")
public class TableVarRenderImpl implements TableVarRender {
    public static final String R_CONTRACT = "YS_02__001"; //应收账款基础合同
    public static final String R_FAPIAO = "YS_02__002"; //应收账款发票列表
    public static final String R_OTHERBILL = "YS_02__003"; //应收账款其它票据

    @Override
    public String renderMode(JSONObject context, ContractVo contractVo, VarItems sysVarItem) {

        String sn = sysVarItem.getSn();
        if(StringUtils.isBlank(sn)) {
            return "failure";
        }

        /**
         * 基础交易合同
         */
        else if(R_CONTRACT.equals(sn)) {
            List<BaseContract> contractList = (List<BaseContract>)context.get("contractList");
            JSONArray jarr = new JSONArray();
            f:{
                if(contractList == null || contractList.size() == 0) {
                    break f;
                }
                for(int i=0; i<contractList.size(); i++) {
                    BaseContract baseContract = contractList.get(i);

                    JSONObject obj = new JSONObject()
                            .fluentPut("index", ""+(i+1))
                            .fluentPut("buyerCompany", baseContract.getBuyerCompany())
                            .fluentPut("contractCode", baseContract.getContractCode())
                            .fluentPut("contractName", baseContract.getContractName())
                            .fluentPut("contractDate", baseContract.getContractDate())
                            .fluentPut("goods", baseContract.getGoods())
                            ;
                    jarr.add(obj);
                }
            }
            if(jarr.size() == 0) {
                jarr.add(new JSONObject()); // 没有就相当于加一个空行
            }
            contractVo.getSysParam().fluentPut(sn, jarr); // 暴露一个变量
            contractVo.addHtmlSpecialTag("tr", "${"+ sn +"}", sn);
            return "success";
        }

        /**
         * 发票列表
         */
        else if(R_FAPIAO.equals(sn)) {
            List<Fapiao> fapiaoList = (List<Fapiao>)context.get("fapiaoList");
            JSONArray jarr = new JSONArray();
            f:{
                if(fapiaoList == null || fapiaoList.size() == 0) {
                    break f;
                }
                for(int i=0; i<fapiaoList.size(); i++) {
                    Fapiao fapiao = fapiaoList.get(i);

                    JSONObject obj = new JSONObject()
                            .fluentPut("index", ""+(i+1))
                            .fluentPut("buyerCompany", fapiao.getBuyerCompany())
                            .fluentPut("fpDate", fapiao.getFpDate())
                            .fluentPut("fpNo", fapiao.getFpNo())
                            .fluentPut("fpAmount", fapiao.getFpAmount())
                            ;
                    jarr.add(obj);
                }
            }
            if(jarr.size() == 0) {
                jarr.add(new JSONObject()); // 没有就相当于加一个空行
            }
            contractVo.getSysParam().fluentPut(sn, jarr); // 暴露一个变量
            contractVo.addHtmlSpecialTag("tr", "${"+ sn +"}", sn);
            return "success";
        }

        /**
         * 其它票据
         */
        else if(R_OTHERBILL.equals(sn)) {
            List<OtherBill> otherBillList = (List<OtherBill>)context.get("otherBillList");
            JSONArray jarr = new JSONArray();
            f:{
                if(otherBillList == null || otherBillList.size() == 0) {
                    break f;
                }
                for(int i=0; i<otherBillList.size(); i++) {
                    OtherBill otherBill = otherBillList.get(i);

                    JSONObject obj = new JSONObject()
                            .fluentPut("index", ""+(i+1))
                            .fluentPut("buyerCompany", otherBill.getBuyerCompany())
                            .fluentPut("billCode", otherBill.getBillCode())
                            .fluentPut("billType", otherBill.getBillType())
                            .fluentPut("billDate", otherBill.getBillDate())
                            .fluentPut("unitPrice", otherBill.getUnitPrice())
                            .fluentPut("netWeight()", otherBill.getNetWeight())
                            .fluentPut("amount", otherBill.getAmount())
                            ;
                    jarr.add(obj);
                }
            }
            if(jarr.size() == 0) {
                jarr.add(new JSONObject()); // 没有就相当于加一个空行
            }
            contractVo.getSysParam().fluentPut(sn, jarr); // 暴露一个变量
            contractVo.addHtmlSpecialTag("tr", "${"+ sn +"}", sn);
            return "success";
        }

        return "failure";
    }
}
