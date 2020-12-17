package com.scf.erdos.factoring.contract.model.businessData;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description : 企业信息
 * @author：bao-clm
 * @date: 2020/8/14
 * @version：1.0
 */

@Data
public class Company implements Serializable {

      private String companyName; //企业名称
      private String creditNo; //统一社会信用代码
      private String registeredAddr; //公司注册地址
      private String letterAddr; //公司通信地址
      private String companyEmail; //企业邮箱
      private String companyFax; //企业传真
      private String companyAccountAddr; //企业开户行地址
      private String companyAccount; //企业开户行账号
      private String accountName; //收款账户名称
      private String bankBranch; //开户行网点名称
      private String bankNum; //开户行行号
      private String legalName; //法人姓名
      private String legalIdNo; //法人身份证号
      private String legalMobile; //法人电话
      private String controManInfo; //实际控制人简介
      private String shareholderName1; //第一股东名称
      private String contacterName; //联系人名称
      private String contacterMobile; //联系人手机号
      private String contacterWechat; //联系人微信
      private String contacterPosition; //联系人职务
      private String contacterEmail; //联系人邮箱
}
