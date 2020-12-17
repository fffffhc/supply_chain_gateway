package com.scf.erdos.factoring.vo.yszk;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description : 发票Excel 模板解析
 * @author：bao-clm
 * @date: 2020/8/20
 * @version：1.0
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class FapiaoExcelVo extends BaseRowModel {

    @ExcelProperty(value="发票号码",index = 0)
    private String fpNo;

    @ExcelProperty(value="发票代码",index = 1)
    private String fpCode;

    @ExcelProperty(value="买方企业名称",index = 2)
    private String buyerCompany;

    @ExcelProperty(value="发票日期",index = 3)
    private String fpDate;

    @ExcelProperty(value="发票金额（含税/元）",index = 4)
    private String fpAmount;

}
