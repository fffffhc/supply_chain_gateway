package com.scf.erdos.factoring.contract.model.businessData;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description : 合同模板
 * @author：bao-clm
 * @date: 2020/8/13
 * @version：1.0
 */

@Data
public class ContractTemplate implements Serializable {
    private Integer id;
    private String code;
    private String contractName;//合同名称
    private String sourceHtml;//原模板消息
    private String varGroupListStr;//业务自定义变量 json数组 字符串
    private String type;//合同类型（融资，置换）
    private String createTime;//创建时间
}
