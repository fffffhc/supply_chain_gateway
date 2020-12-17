package com.scf.erdos.factoring.vo.yszk;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description : 应收账款发票 视图实体类
 * @author：bao-clm
 * @date: 2020/5/12
 * @version：1.0
 */

@Data
public class ReceivableFapiaoVo implements Serializable {

    /**
     * 主键
     */
    private String id;
    /**
     * 应收账款id
     */
    private String yszkId;
    /**
     * 发票号码
     */
    private String fpNo;
    /**
     * 发票代码
     */
    private String fpCode;
    /**
     * 发票日期
     */
    private String fpDate;
    /**
     * 金额
     */
    private Double fpAmount;
    /**
     * 买方企业
     */
    private String buyerCompany;

}
