package com.scf.erdos.factoring.vo.company;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description : 企业详情视图信息
 * @author：bao-clm
 * @date: 2020/5/11
 * @version：1.0
 */
@Data
@SuppressWarnings("all")
public class CompanyInfoVo implements Serializable {

    private Integer id;//主键
    private String companyCode;//标识
    private String companyLabel1;//标签
    private String companyName;//企业名称
    private String companyType;//企业类型
    private String companyTypeName;//企业类型名称
    private String industryType;//行业类型
    private String industryTypeName;//行业类型名称
    private Integer businessScale;//经营规模（1，小型；2，中型；3，大型）
    private String businessScope;//经营范围
    private String registeredAddr;//公司注册地址
    private String letterAddr;//公司通信地址
    private String registeredTime;//公司注册时间
    private String registeredCapital;//公司注册资本
    private String companyEmail;//企业邮箱
    private String companyFax;//企业传真
    private String creditNo;//统一社会信用代码
    private String businessLic;//营业执照（影像）
    private String companyAccountAddr;//企业开户行地址
    private String companyAccount;//企业开户行账号
    private String accountName;//收款账户名称
    private String bankBranch;//开户行网点名称
    private String bankNum;//开户行行号
    private String fapiaoInfo;//开票信息盖章（影像）
    private String legalName;//法人姓名
    private String legalIdFront;//法人身份证正面
    private String legalIdOther;//法人身份证反面
    private String legalIdNo;//法人身份证号
    private String legalMobile;//移动电话
    private String controManInfo;//实际控制人简介
    private String shareholderName1;//第一股东名称
    private String shareholderCapital1;//认缴注册资本（第一股东）
    private String shareholderName2;//第二股东名称
    private String shareholderCapital2;//认缴注册资本（第二股东）
    private String contacterName;//联系人名称
    private String contacterMobile;//联系人手机号
    private String contacterWechat;//联系人微信
    private String contacterPosition;//联系人职务
    private String contacterEmail;//联系人邮箱
    private String financialReports;//财务报告
    private String auditReport;//审计报告
    private String companyCreditReport;//企业信用报告
    private String historicalTradeContract;//历史贸易合同
    private String statusDesc;//审核描述
    private String userId;//联系人账号id
    private String other;//其它

}
