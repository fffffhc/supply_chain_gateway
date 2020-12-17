package com.scf.erdos.factoring.vo.workflow;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description : 工作流
 * @author：bao-clm
 * @date: 2020/7/20
 * @version：1.0
 */

@Data
public class WorkflowPageVo implements Serializable {

    private Integer id;
    private String name;
    private String userId;
    private String userName;
    private String companyId;
    private String companyName;
    private String flowType;
    private String status;
    private String createTime;
}
