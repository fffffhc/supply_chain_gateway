package com.scf.erdos.factoring.contract.model;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description :
 * @author：bao-clm
 * @date: 2020/8/13
 * @version：1.0
 */

@Data
public class FddContract {

    /** 主要所属人id（这里取业务发起人） */
    private Long ownerId;

    /** 主要所属人名称（这里取业务发起人） */
    private String ownerName;

    /** 接口请求记录编号 */
    private String apiRequestOutRecordSn;

    // --- 从模板上获取的一些信息记录 start

    /** 模板唯一号 */
    private String templateUuid;

    /** 模板分类
     *  */
    private String templateCategory;

    /**
     * 模板类型（签署阶段）
     */
    private String templateType;

    // --- 从模板上获取的一些信息记录 end

    /** 关联类型 */
   // private RelType relType;

    /** 关联id */
    private Long relId;

    /** 关联业务编号 */
    private String relSn;

    /** 关联明细项id */
    private Long relItemId;

    /** 业务显示编号 */
    private String showSn;

    /** 显示编号序号 */
    private Integer showSnSeq;

    /** 合同编号 */
    private String sn;

    /** 最后一次签署时间 */
    private Date lastSignDate;

    /** 合同标题 */
    private String docTitle;

    /** 文档地址 */
    private String docUrl;

    /** 文档类型 */
    private String docType;

    /** 合同日期 */
    private Date contractDate;

    /**上传时间*/
    private Date uploadDate = new Date();

    /**
     * 填入模板的参数json字符串
     */
    private String paramStr;

    /** 动态表单数据 */
    private String dynamicTablesStr;

    /** 合同下载地址
     * */
    private String downloadUrl;

    /** 合同查看地址
     * */
    private String viewPdfUrl;


    /**
     * 是否已归档，默认false
     */
    private Boolean isFiling = false;

    /** 产品编号 */
    private String productSn;

    /** 资产项目编号 */
    private String assetProjectConfigSn;

    /** 期数（第几期），主要是生成摘牌的每期付款合同要用 */
    private Integer periodNum;

    /** 生成划款合同的批次号 */
    private String generatePayContractBatchNo;

    // --- 基础字段  end

    /** 签署方 */
    @TableField(exist = false)
    private List<FddExtSign> fddExtSigns = new ArrayList<>();

    /** 临时字段 */
    @TableField(exist = false)
    private JSONObject temp = new JSONObject();

}
