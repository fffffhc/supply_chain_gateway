package com.scf.erdos.factoring.model.contract;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description : 电子合同模板
 * @author：bao-clm
 * @date: 2020/9/2
 * @version：1.0
 */

@Data
public class ContractTemplate implements Serializable {

    private Integer id;
    private String contractName;
    private String contractTemplateUrl;
    private String sourceHtml;
    private String varGroupListStr;
    private Integer type;
    private Long userId;
    private String companyId;
    private String templateExplain;

}
