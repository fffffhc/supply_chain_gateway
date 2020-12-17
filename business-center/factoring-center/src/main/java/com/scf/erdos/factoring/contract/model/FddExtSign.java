package com.scf.erdos.factoring.contract.model;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * @Description :
 * @author：bao-clm
 * @date: 2020/8/13
 * @version：1.0
 */
@Data
public class FddExtSign {
    /** 签署方id，这里是企业id */
    private Long signerId;

    /** 签署方名称，这里是企业名称 */
    private String signerName;

    /** 签署方标识，取
     *  */
    private String signature;

    /** 状态 */
   // private Status status = Status.created;

    /** 接口请求失败原因 */
    private String failureReason;

    /** 接口请求记录编号 */
    private String apiRequestOutRecordSn;

    /** 法大大客户编号*/
    private String fddCustomerId;

    /** 合同编号 */
    private String contractSn;

    /** 签署定位关键字（用于定位盖章） */
    private String signKeyword;

    /** 是否盖骑缝章 */
    private Boolean isAcrossPage = false;

    /** 骑缝章客户id */
    private String acrossPageCustomerId;

    /** 交易号 */
    private String upSendSn;

    /** 合同标题 */
    private String docTitle = "";

    /** 客户类型 批量自动签特有参数 必传
     * 1-个人； 2-企业
     *  */
    private String clientType;

    /**客户角色 自动签特有参数 必传
     1-接入平台；
     2-仅适用互金行业担保公司或担保人；
     3-接入平台客户（互金行业指投资人）；
     4-仅适用互金行业借款企业或者借款人*/
    private String clientRole;

    /** 授权类型 （这个只有在签署授权合同时才会用到）
     * 1:授权自动签（目前只能填 1）
     * */
    private String authType;

    /** 签署序号，都是0表示的是无序签署，如果为1,2,3则表示顺序签署 */
    private Integer signSeq = 0;

    /** 当前是否可以签署 */
    private Boolean isAbleSign = false;

    /**异步回调地址*/
    private String upNotifyUrl = "";

    /** 同步回调地址 */
    private String upReturnUrl = "";

    /** 签署地址 */
    private String signUrl;

    /** 签署时间 */
    private Date signDate;

    /** 下载地址 */
    private String downloadUrl;

    /** 查看地址 */
    private String viewPdfUrl;

// --- 基础字段  end

    @TableField(exist = false)
    private FddContract fddContract;

    /** 临时字段 */
    @TableField(exist = false)
    private JSONObject temp;
}
