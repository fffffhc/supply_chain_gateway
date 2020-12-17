package com.scf.erdos.factoring.vo.contract;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description : 合同模板
 * @author：bao-clm
 * @date: 2020/9/1
 * @version：1.0
 */
@Data
public class ContractTemplateVo implements Serializable {

    private String contractName;
    private String sourceHtml;
    private String varGroupListStr;
    private Integer type;
    private String explain;
}
