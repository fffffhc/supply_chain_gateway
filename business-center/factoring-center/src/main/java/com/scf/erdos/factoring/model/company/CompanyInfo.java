package com.scf.erdos.factoring.model.company;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description : 企业详情信息实体类
 * @author：bao-clm
 * @date: 2020/5/8
 * @version：1.0
 */
@Data
@TableName("company_info")
public class CompanyInfo implements Serializable {

        private Integer id;//主键
        private String companyCode;//标识
        private String companyName;//企业名称
        private String companyType;//企业类型
        private String industryType;//行业类型
        private Integer businessScale;//经营规模（1，小型；2，中型；3，大型）
        private String businessScope;//经营范围
        private String registeredAddr;//公司注册地址
        private String letterAddr;//公司通信地址
        private Date registeredTime;//公司注册时间
        private Double registeredCapital;//公司注册资本
        private String companyEmail;//企业邮箱
        private String companyFax;//企业传真
        private String creditNo;//统一社会信用代码
        private String businessLic;//营业执照（影像）
        private String companyAccountAddr;//企业开户行地址
        private String companyAccount;//企业开户行账号
        private String fapiaoInfo;//开票信息盖章（影像）
        private String legalName;//法人姓名
        private String legalIdFront;//法人身份证正面
        private String legalIdOther;//法人身份证反面
        private String legalIdNo;//法人身份证号
        private String controManInfo;//实际控制人简介
        private String legalMobile;//移动电话
        private String shareholderName_1;//第一股东名称
        private Double shareholderCapital_1;//认缴注册资本（第一股东）
        private String shareholderName_2;//第二股东名称
        private Double shareholderCapital_2;//认缴注册资本（第二股东）
        private String contacterName;//联系人名称
        private String contacterMobile;//联系人手机号
        private String contacterWechat;//联系人微信
        private String contacterPosition;//联系人职务
        private String contacterEmail;//联系人邮箱
        private String financialReports;//财务报告
        private String auditReport;//审计报告
        private String companyCreditReport;//企业信用报告
        private String historicalTradeContract;//历史贸易合同
        private String other;//其它
        private String userId;//创建用户
        private String status;//状态（0，删除；1，待审核；2，驳回；3，审核通过）
        private String caStatus;//ca认证（1，未提交；2，已提交；3，未通过；4，已通过）
        private Date createTime;//创建时间
        private Date updateTime;//更新时间

}
