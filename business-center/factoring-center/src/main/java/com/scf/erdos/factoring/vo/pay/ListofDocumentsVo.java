package com.scf.erdos.factoring.vo.pay;

import lombok.Data;

import java.util.Date;

/**
 * @author FUHAOCHENG
 * @version 1.0
 * @date 2020/9/16 10:21
 */

@Data
public class ListofDocumentsVo {
    private String  companyName;//融资方名称
    private String  code;//融资编号
    private String  name;//产品名称
    private Integer status;//融资状态
    private Date    applyTime;//融资申请日期
}
