package com.shiyuan.sharingbaseontimemode;

import org.junit.jupiter.api.Test;

public class SharingBaseOnTimeModeApplicationTests {

    @Test
    void contextLoads() throws ClassNotFoundException {

        TestClz.Inner inner = new TestClz.Inner();
        Class<?> aClass = Class.forName(TestClz.Inner.class.getName());
    }


    public static void main(String[] args) throws ClassNotFoundException {
        Class<?> aClass = Class.forName(TestClz.Inner.class.getName());
        System.out.println(aClass);
    }
}
