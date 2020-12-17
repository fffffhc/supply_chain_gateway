package com.scf.erdos.factoring.model.workflow;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description : 工作流详情
 * @author：bao-clm
 * @date: 2020/5/20
 * @version：1.0
 */

@Data
public class CompanyWorkflowInfo implements Serializable {
    private String roleId;
    private String code;
    private String roleName;
    private String status;
    private String sort;
}
