package com.scf.erdos.factoring.vo.financing;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description : 融资申请
 * @author：bao-clm
 * @date: 2020/8/5
 * @version：1.0
 */

@Data
public class FinancingAuditLog implements Serializable {

    private Integer id;
    private Integer financingId;
    private String auditOpinion;
    private Integer userId;
    private String roleCode;
    private String status;
    private String createTime;
}
