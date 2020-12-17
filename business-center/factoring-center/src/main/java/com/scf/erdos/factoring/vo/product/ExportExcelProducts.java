package com.scf.erdos.factoring.vo.product;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

/**
 * @Description : 占位符Excel 导出
 * @author：bao-clm
 * @date: 2020/7/30
 * @version：1.0
 */

@Data
public class ExportExcelProducts extends BaseRowModel {

    @ExcelProperty(value="编号",index = 1)
    private String id;

    @ExcelProperty(value="占位符",index = 1)
    private String placeholder;

    @ExcelProperty(value="占位符名称",index = 1)
    private String placeholderName;

}
