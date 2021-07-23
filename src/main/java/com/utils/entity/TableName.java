package com.utils.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Description:
 * @Author Zengfc
 * @Date 2021/7/7 16:52
 * @Version 1.0
 */

@Data
public class TableName {
    @ExcelProperty(value = "表名")
    private String tableName;
    @ExcelProperty(value = "字段名称")
    private String columnName;
    @ExcelProperty(value = "字段类型")
    private String dataType;
    @ExcelProperty(value = "字段长度")
    private String dataLength;
    @ExcelProperty(value = "是否可为空")
    private String nullAble;
    @ExcelProperty(value = "字段说明")
    private String comments;

}
