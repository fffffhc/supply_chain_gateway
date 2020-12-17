package com.scf.erdos.factoring.vo.pay;

import lombok.Data;

import java.util.Date;

/**
 * @author FUHAOCHENG
 * @version 1.0
 * @date 2020/9/17 9:42
 */

@Data
public class CreditDecisionVo {
    private String  code;//融资编号
    private String  fundingCompany;//授信机构
    private String  customerCompany;//客户名称
    private String  creditNo;//统一社会信用代码
    private String  customerLevel;//客户等级
    private String  creditCode;//授信编号
    private Double  creditLine;//授信额度
    private Date    creditStartTime;//授信起始日
    private Date    creditEndTime;//授信到期日
    private String  name;//授信产品
    private Integer guaranteeType;//担保方式
}

