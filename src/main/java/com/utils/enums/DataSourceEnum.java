package com.utils.enums;

import org.omg.CORBA.PUBLIC_MEMBER;

/**
 * @Description:
 * @Author Zengfc
 * @Date 2021/7/9 10:17
 * @Version 1.0
 */
public enum DataSourceEnum {

    MYSQL("com.mysql.jdbc.Driver"),
    ORACLE("oracle.jdbc.OracleDriver");


    private final String driver;

    DataSourceEnum(String driver){
        this.driver = driver;
    }

    public String getDriver() {
        return driver;
    }
}
