package com.scf.erdos.common.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description : 企业信息
 * @author：bao-clm
 * @date: 2020/7/8
 * @version：1.0
 */

@Data
public class CompanyInfoVo implements Serializable {
    private Integer id;//主键
    private String companyAccount;//
    private String status;//状态（0，删除；1，待审核；2，驳回；3，审核通过）
    private String caStatus;//ca认证（1，未提交；2，已提交；3，未通过；4，已通过）
}
