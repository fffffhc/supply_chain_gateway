package com.scf.erdos.factoring.contract.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.scf.erdos.factoring.contract.model.businessData.Fapiao;
import com.scf.erdos.factoring.contract.model.businessData.OtherBill;
import lombok.Data;

import java.util.List;
import java.util.Optional;

/**
 * @Description :
 * @author：bao-clm
 * @date: 2020/8/12
 * @version：1.0
 */

@Data
public class Project {
    private Integer productId;//产品id

    private Integer financingId;//融资申请id

    private List<Fapiao> fapiaos;//应收账款发票列表

    private List<OtherBill> othserBills;//应收账款其它票据列表


    /** 业务显示编号 */
    private String showSn;
    /** 业务显示编号序号 */
    private Integer showSnSeq;

    /** 技术信息，json字符串 */
    private String techInfo;



    /**
     * 获取技术信息对象
     * @return
     */
    public JSONObject getTechInfoObj() {
        return Optional.ofNullable(JSON.parseObject(getTechInfo())).orElse(new JSONObject());
    }
}
