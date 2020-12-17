package com.scf.erdos.factoring.vo.company;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description : 企业分页列表 视图信息
 * @author：bao-clm
 * @date: 2020/5/8
 * @version：1.0
 */

@Data
public class CompanyPageVo implements Serializable {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 企业一二级标签
     */
    private String companyLabel1;
    private String companyLabel1Name;
    private String companyLabel2;

    /**
     * 统一社会信用代码
     */
    private String creditNo;
    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 注册时间
     */
    private String registeredTime;

    /**
     * 录入用户id
     */
    private String userId;

    /**
     * 联系人登陆账号
     */
    private String account;

    /**
     * 联系人
     */
    private String contacterName;

    /**
     * 联系人电话
     */
    private String contacterMobile;

    /**
     * 状态
     */
    private String status;

    /**
     * 审核描述
     */
    private String statusDesc;
}
