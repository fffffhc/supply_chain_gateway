package com.scf.erdos.factoring.vo.credit;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description : 授信申请列表
 * @author：bao-clm
 * @date: 2020/7/28
 * @version：1.0
 */

@Data
public class CreditPageVo implements Serializable {

    private Integer id;
    private String code;
    private String customerName;
    private String fundingCompanyId;
    private String fundingCompanyName;
    private String productName;
    private double creditLine;
    private String creditStartTime;
    private String creditEndTime;
    private int status;
    private Boolean statusActive;//true 按钮展示 ， false 按钮不展示
    private String userId;
}
