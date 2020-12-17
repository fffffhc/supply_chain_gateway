package com.scf.erdos.factoring.vo.signupOnline;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description : 在线签约合同
 * @author：bao-clm
 * @date: 2020/8/7
 * @version：1.0
 */

@Data
public class SignupOnlineVo implements Serializable {

    private Integer id;
    private String creditCode;//授信编号
    private String financingCode;//融资编号
    private String customerCompanyId;//融资方id
    private String customerCompanyName;//融资方名称
    private String fundingCompanyId;//资金机构id
    private String fundingCompanyName;//资金机构名称
    private String signupCompanyId;//签约方企业id
    private String contractId;//合同id
    private String contractName;//合同名称
    private String contractUrl;//合同url
    private String productId;//产品/项目id
    private String productName;//产品/项目名称
    private String contractCreateTime;//合同生成日期
    private String status;//合同状态（0，待签约；1，部分签约；2，已成功）
    private String createTime;//创建时间
}
