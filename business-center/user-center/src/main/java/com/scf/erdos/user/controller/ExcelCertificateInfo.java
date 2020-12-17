package com.scf.erdos.user.controller;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

@Data
public class ExcelCertificateInfo implements Serializable {

    @Excel(name = "姓名", height = 18, width = 30, isImportField = "true_st")
    private String name;

    @Excel(name = "性别", replace = { "男_0", "女_1" }, isImportField = "true_st")
    private String sex;

    @Excel(name = "身份证号", height = 18, width = 30, isImportField = "true_st")
    private String idNum;

    @Excel(name = "学历", replace = { "小学_0","初中_1","高中_2","中专_3","专科_4","本科_5","硕士_6","博士_7" }, isImportField = "true_st")
    private String education;

    @Excel(name = "证书类型", replace = { "专业人才技能证书_1", "职业技能培训证书_2" }, isImportField = "true_st")
    private String certificateType;

    @Excel(name = "专业名称/岗位工种", height = 18, width = 30, isImportField = "true_st")
    private String certificateName;

    @Excel(name = "等级", replace = { "高级_0", "中级_1","初级_2" ,"师资_3" }, isImportField = "true_st")
    private String certificateLevel;

    @Excel(name = "证书编号", height = 18, width = 30, isImportField = "true_st")
    private String certificateNum;

    @Excel(name = "理论成绩", height = 18, width = 30, isImportField = "true_st")
    private String theoryScore;

    @Excel(name = "实操成绩", height = 18, width = 30, isImportField = "true_st")
    private String operationScore;

    @Excel(name = "考试结果", height = 18, width = 30, isImportField = "true_st")
    private String result;

    @Excel(name = "发证日期", height = 18, width = 30, isImportField = "true_st")
    private String issueTime;

    @Excel(name = "签发单位", height = 18, width = 30, isImportField = "true_st")
    private String department;

    @Excel(name = "联系人", height = 18, width = 30, isImportField = "true_st")
    private String linkName;

    @Excel(name = "电话", height = 18, width = 30, isImportField = "true_st")
    private String phone;

    @Excel(name = "邮寄地址", height = 18, width = 30, isImportField = "true_st")
    private String address;
}
