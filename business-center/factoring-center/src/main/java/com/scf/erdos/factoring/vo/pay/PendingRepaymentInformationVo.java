package com.scf.erdos.factoring.vo.pay;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * @author FUHAOCHENG
 * @version 1.0
 * @date 2020/9/7 10:24
 */

@Data
public class PendingRepaymentInformationVo implements Serializable {
    private Integer creditId;//授信编号
    private String  code;//融资编号
    private String  name;//产品名称
    private String  companyName;//资金机构
    private Integer businessType;//业务标志
    private Double  balance;//融资余额
    private char    payType;//还款方式
    private Integer    interestPayDay;//还息日
    private Integer isPcertainRate;//利率（年化/%）
    private Date    receivableTime;//应收账款到期日
    private Date    financingEndTime;//融资到期日
    private Integer status;//融资状态


}
