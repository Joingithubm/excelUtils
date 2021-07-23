package com.utils.vo;

import com.utils.enums.DataSourceEnum;
import lombok.Data;



/**
 * @Description:
 * @Author Zengfc
 * @Date 2021/7/9 14:23
 * @Version 1.0
 */
@Data
public class DataSourceVo {


    private String host;
    private String port;
    private String serviceName;
    private String username;
    private String password;

    public static void main(String[] args) {
        String str = "IVR_POLICY_WORKORDER_NOTACCEPTED_62";
        System.out.println(str.length());

        String str1= str.subSequence(0,20)+"__over_"+100;
        System.out.println(str1);
        System.out.println(str1.length());
    }
}
