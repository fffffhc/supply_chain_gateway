package com.scf.erdos.factoring.vo.credit;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description : 授信申请审核日志
 * @author：bao-clm
 * @date: 2020/7/29
 * @version：1.0
 */

@Data
public class ProductCreditLog implements Serializable {

    private Integer id;
    private Integer productCreditId;
    private String creditOpinion;
    private String userId;
    private String userName;
    private String status;
    private String createTime;
}
