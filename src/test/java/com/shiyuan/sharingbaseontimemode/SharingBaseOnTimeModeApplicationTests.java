package com.shiyuan.sharingbaseontimemode;

import com.alibaba.fastjson.JSON;
import com.shiyuan.sharingbaseontimemode.entity.UserInfo;
import com.shiyuan.sharingbaseontimemode.manager.OrderInfoManager;
import com.shiyuan.sharingbaseontimemode.mapper.OrderInfoMapper;
import com.shiyuan.sharingbaseontimemode.mapper.UserInfoDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class SharingBaseOnTimeModeApplicationTests {

    @Test
    void contextLoads() throws ClassNotFoundException {

        TestClz.Inner inner = new TestClz.Inner();
        Class<?> aClass = Class.forName(TestClz.Inner.class.getName());
    }

    @Autowired
    private UserInfoDao userInfoDao;


    @Test
    public void insertUserInfo(){
        for (int i = 0; i < 100; i++) {
            UserInfo temp = new UserInfo();
            temp.setId(OrderInfoManager.nextOrderId());
            temp.setName(UUID.randomUUID().toString());
            temp.setSex(1);
            temp.setStatus(1);
            temp.setCreateTime(new Date());
            userInfoDao.insertSelective(temp);
        }
    }

    @Test
    public void queryUserInfos(){
        List<UserInfo> userInfos = userInfoDao.queryUserInfos(new Date());
        System.out.println(JSON.toJSONString(userInfos));
    }


    public static void main(String[] args) throws ClassNotFoundException {
        Class<?> aClass = Class.forName(TestClz.Inner.class.getName());
        System.out.println(aClass);
    }
}
