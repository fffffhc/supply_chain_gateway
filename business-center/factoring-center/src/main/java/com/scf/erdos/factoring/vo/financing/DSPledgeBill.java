package com.scf.erdos.factoring.vo.financing;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description : 质押票据列表
 * @author：bao-clm
 * @date: 2020/8/4
 * @version：1.0
 */

@Data
public class DSPledgeBill implements Serializable {

    private Integer id;
    private String num;//票号
    private double billMoney;//票据金额
    private String userName;//兑现人
    private String endTime;//到期日
}
