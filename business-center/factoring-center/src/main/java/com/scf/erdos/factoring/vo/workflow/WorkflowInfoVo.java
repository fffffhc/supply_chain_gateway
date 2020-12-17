package com.scf.erdos.factoring.vo.workflow;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description : 工作流详情
 * @author：bao-clm
 * @date: 2020/7/21
 * @version：1.0
 */

@Data
public class WorkflowInfoVo implements Serializable {

    private String id;
    private String name;
    private String companyId;
    private String companyName;
    private String flowType;
    private String flowInfo;
    private String status;
    private String createTime;
    private List<CompanyWorkflow> flowInfoList;
}
