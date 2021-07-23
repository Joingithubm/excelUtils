package com.utils.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author Zengfc
 * @Date 2021/7/9 9:44
 * @Version 1.0
 */
public interface CommonMapper {

    /**
     * 查询表名
     * @return
     */
    List<Map<String,Object>> selectDataSource();

    /**
     * 查询表结构
     * @param tableName
     * @return
     */
    List<Map<String,Object>> selectTableToMap(@Param(value = "tableName")String tableName);

}
