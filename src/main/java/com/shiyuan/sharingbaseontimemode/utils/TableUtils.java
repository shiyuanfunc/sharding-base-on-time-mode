package com.shiyuan.sharingbaseontimemode.utils;

/**
 * @Author MUSI
 * @Date 2022/7/3 12:17 AM
 * @Description
 * @Version
 **/
public class TableUtils {

    public static String subTableName(String orderId){
        return orderId.substring(2, 6);
    }
}
