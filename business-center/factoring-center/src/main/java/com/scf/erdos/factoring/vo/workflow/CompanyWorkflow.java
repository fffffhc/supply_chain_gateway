package com.scf.erdos.factoring.vo.workflow;

import lombok.Data;

/**
 * @Description : 角色id
 * @author：bao-clm
 * @date: 2020/7/21
 * @version：1.0
 */

@Data
public class CompanyWorkflow {
    private String roleId;//角色Id
    private String code;//角色Code
    private String roleName;//角色名称
    private int status;//操作状态
    private String sort;//排序
}
