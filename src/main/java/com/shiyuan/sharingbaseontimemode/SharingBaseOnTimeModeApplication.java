package com.shiyuan.sharingbaseontimemode;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.shiyuan.sharingbaseontimemode.mapper")
public class SharingBaseOnTimeModeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SharingBaseOnTimeModeApplication.class, args);
    }

}
