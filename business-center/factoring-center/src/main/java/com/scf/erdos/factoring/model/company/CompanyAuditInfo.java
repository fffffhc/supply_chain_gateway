package com.scf.erdos.factoring.model.company;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description : 企业审核过程信息实体类
 * @author：bao-clm
 * @date: 2020/5/8
 * @version：1.0
 */

@Data
public class CompanyAuditInfo implements Serializable {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 企业主键
     */
    private String companyId;

    /**
     * 审核状态（1，待审核；2，驳回；3，审核通过）
     */
    private String status;

    /**
     * 描述
     */
    private String statusDesc;

    /**
     * 审核人主键
     */
    private String userId;

    /**
     * 审核时间
     */
    private Date auditTime;
}
