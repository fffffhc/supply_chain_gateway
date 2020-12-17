package com.scf.erdos.factoring.contract.model.businessData;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description : 票据质押
 * @author：bao-clm
 * @date: 2020/12/3
 * @version：1.0
 */

@Data
public class DSPledgeBill implements Serializable {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

    private String num;//票号
    private BigDecimal billMoney;//票据金额小写
    private String userName;//兑现人
    private Date endTime;//到期日
    public String getEndTime() {
        if(endTime == null || "".equals(endTime)){
            return null;
        }
        return sdf.format(endTime);
    }

}
