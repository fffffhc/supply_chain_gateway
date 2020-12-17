package com.scf.erdos.factoring.model.workflow;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description : 工作流实体类
 * @author：bao-clm
 * @date: 2020/5/20
 * @version：1.0
 */

@Data
public class CompanyWorkflow implements Serializable {
    /**
     * 主键
     */
    private String id;
    /**
     * 工作流程名称
     */
    private String name;
    /**
     * 创建工作流公司id
     */
    private String companyId;
    /**
     * 工作流类型
     */
    private String flowType;
    /**
     * 流程内容（json格式）
     */
    private List<CompanyWorkflowInfo> flowInfoList;
    private String flowInfo;
    /**
     * 0,新建；1，启用；2，停用
     */
    private String status;
    /**
     * 创建用户id
     */
    private Long userId;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 修改用户id
     */
    private Long updateUserId;
    /**
     * 修改时间
     */
    private String updateTime;

}
