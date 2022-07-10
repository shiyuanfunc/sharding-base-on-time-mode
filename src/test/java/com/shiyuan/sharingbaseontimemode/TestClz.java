package com.shiyuan.sharingbaseontimemode;

import com.shiyuan.sharingbaseontimemode.utils.TableUtils;
import lombok.Data;

/**
 * @Author MUSI
 * @Date 2022/7/2 10:10 PM
 * @Description
 * @Version
 **/
@Data
public class TestClz {

    private String name;

    @Data
    public static class Inner{
        private Integer age;
    }

    public static void main(String[] args) {

        String str = "20220702150412000";
        String substring = TableUtils.subTableName(str);
        System.out.println(substring);
    }

}
