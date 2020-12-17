package com.scf.erdos.factoring.contract.model.businessData;

import com.scf.erdos.factoring.util.financialUtils.NumberToCN;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description : 授信信息
 * @author：bao-clm
 * @date: 2020/8/12
 * @version：1.0
 */

@Data
public class Credit implements Serializable {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

    private String code;//授信编号
    private BigDecimal creditLine;//授信额度小写
    private String creditLineToCN;//授信额度大写
    public String getCreditLineToCN() {
        return NumberToCN.number2CNMontrayUnit(creditLine);
    }

    private Date creditStartTime;//授信起始日
    private Date creditEndTime;//授信到期日

    public String getCreditStartTime() {
        if(creditStartTime == null || "".equals(creditStartTime)){
            return null;
        }
        return sdf.format(creditStartTime);
    }

    public String getCreditEndTime() {
        if(creditEndTime == null || "".equals(creditEndTime)){
            return null;
        }
        return sdf.format(creditEndTime);
    }

}
