package com.scf.erdos.factoring.contract.model.businessData;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description : 签署方合同
 * @author：bao-clm
 * @date: 2020/8/13
 * @version：1.0
 */

@Data
public class UserContract implements Serializable {

    private String creditCode; //授信编号
    private String financingCode; //融资编号
    private Integer customerCompanyId;//融资方id
    private Integer fundingCompanyId;//资金机构id
    private Integer signupCompanyId;//签约方企业id
    private Integer contractId; //合同id
    private String contractName; //合同名称
    private String contractUrl; //合同url
    private Integer productId; //产品/项目id
    private String productName; //产品/项目名称
    private String contractCreateTime; //合同生成日期
    private String createTime; //创建时间
}
