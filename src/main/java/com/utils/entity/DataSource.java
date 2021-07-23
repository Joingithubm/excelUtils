package com.utils.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Description:
 * @Author Zengfc
 * @Date 2021/7/8 15:57
 * @Version 1.0
 */
@Data
public class DataSource {

    @ExcelProperty(value = "表名称")
    private String tableName;
    @ExcelProperty(value = "表说明")
    private String comments;
}
