package com.scf.erdos.uaa.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysClient implements Serializable{

   private static final long serialVersionUID = -8185413579135897885L;
   // 主键
   private Long id;
   // 应用标识
   @ApiModelProperty(value="应用标识",name="clientId")
   private String clientId;
   // 资源限定串(逗号分割)
   private String resourceIds = "";
   // 应用密钥(bcyt) 加密
   private String clientSecret;
   // 应用密钥(明文)
   private String clientSecretStr;
   // 应用范围
   private String scope = "all";
   // 5种oauth授权方式(authorization_code,password,refresh_token,client_credentials)
   private String authorizedGrantTypes = "authorization_code,password,refresh_token,client_credentials";
   // 回调地址
   private String webServerRedirectUri;
   // 权限
   private String authorities = "";
   // access_token有效期
   private Integer accessTokenValidity = 18000;
   // refresh_token有效期
   private Integer refreshTokenValidity = 18000;
   private String additionalInformation = "{}";
   // 是否自动授权 是-true
   private String autoapprove = "true";
   // 状态（0，锁定 1，正常）
   private Boolean status ;
   
}
