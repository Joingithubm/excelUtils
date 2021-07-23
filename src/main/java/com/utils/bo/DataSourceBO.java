package com.utils.bo;

import lombok.Data;

/**
 * @Description:
 * @Author Zengfc
 * @Date 2021/7/9 15:51
 * @Version 1.0
 */
@Data
public class DataSourceBO {

    private String url;
    private String username;
    private String password;
    private String driverClassName;
}
