package com.scf.erdos.factoring.contract.model.businessData;

import lombok.Data;

import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * @Description : 基础合同
 * @author：bao-clm
 * @date: 2020/12/3
 * @version：1.0
 */

@Data
public class BaseContract implements Serializable {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

    /**
     * 合同编号
     */
    private String contractCode;
    /**
     * 合同名称
     */
    private String contractName;
    /**
     * 合同日期
     */
    private String contractDate;
    public String getContractDate() {
        if(contractDate == null || "".equals(contractDate)){
            return null;
        }
        return sdf.format(contractDate);
    }

    /**
     * 商品/服务
     */
    private String goods;
    /**
     * 单价（元、吨）
     */
    private String unitPrice;
    /**
     * 买方企业
     */
    private String buyerCompany;
}
