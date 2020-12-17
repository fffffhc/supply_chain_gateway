package com.scf.erdos.factoring.vo.financing;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description : 融资申请页面参数对象
 * @author：bao-clm
 * @date: 2020/7/31
 * @version：1.0
 */

@Data
public class AppliFinancingVo implements Serializable {

    private Integer productId;//产品id
    private String fundingCompanyName;//资金机构
    private String productName;//融资产品名称
    private Integer financingRate;//融资比例
    private String flowType;//融资方流程类型
    private String payType;//还款方式
    private Integer interestPayDay;//还息日
    private Integer financingInterest;//融资利率
    private Integer factoringServiceRate;//保理服务费率
    private List<Receivable> receivableList;//应收账款
}
