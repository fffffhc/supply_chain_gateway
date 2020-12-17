package com.scf.erdos.factoring.contract.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scf.erdos.factoring.contract.model.businessData.ContractTemplate;
import com.scf.erdos.factoring.contract.model.businessData.UserContract;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description :
 * @author：bao-clm
 * @date: 2020/9/15
 * @version：1.0
 */

@Data
@Slf4j
public class ContractVo implements Serializable {
    private ContractTemplate contractTemplate;
    private List<ContractTemplate> varItems;
    private JSONObject sysParam;
    private JSONObject param;
    JSONArray dynamicTables;
    private Boolean isCompleted = false;

    private UserContract userContract;

    private Date contractDate; // 合同日期
    private String showSn; //业务显示编号
    private Integer showSnSeq; //业务显示编号序号
    private JSONObject temp;
    private String sourceHtml; //原html内容
    private String renderHtml; //渲染数据后的html内容
    private List<JSONObject> htmlSpecialTags; //html的特殊占位符处理

    public ContractVo(ContractTemplate contractTemplate) {
        this.contractTemplate = contractTemplate;
        if (varItems == null) {
            varItems = new ArrayList<>();
        }
        // 排序
        varItems.stream().sorted((o1, o2) -> {
              /*  if(o1.getContractParam() != null && o2.getContractParam() == null) {
                    return -1;
                }else if(o1.getContractParam() == null && o2.getContractParam() != null) {
                    return 1; // o1.getContractParam() 为 null 就调换到后面
                }*/
            return 0;
        });
        this.param = new JSONObject();
        this.sysParam = new JSONObject();
        this.dynamicTables = new JSONArray();
        this.temp = new JSONObject();
    }

    public void addHtmlSpecialTag(String fun, String key, String replaceKey) {
        if (this.htmlSpecialTags == null) {
            this.htmlSpecialTags = new ArrayList<>();
        }
        this.htmlSpecialTags.add(new JSONObject()
                .fluentPut("fun", fun)
                .fluentPut("key", key)
                .fluentPut("replaceKey", replaceKey)
        );
    }

}
