package com.scf.erdos.factoring.vo.yszk;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description : 应收账款分页列表 视图实体类
 * @author：bao-clm
 * @date: 2020/5/12
 * @version：1.0
 */

@Data
public class ReceivablePageVo implements Serializable {

    /**
     * 主键
     */
    private String id;
    /**
     * 待转让资产编号
     */
    private String code;
    /**
     * 资产录入时间
     */
    private String createTime;
    /**
     * 买方名称
     */
    private String buyerCompany;
    /**
     * 资产标签
     */
    private String tag;
    /**
     * 资产录入人id
     */
    private String userId;
    /**
     * 资产录入人
     */
    private String username;
    /**
     * 状态 false,未使用；true,已使用
     */
    private boolean status;
}
