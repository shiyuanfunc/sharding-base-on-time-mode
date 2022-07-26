package com.shiyuan.sharingbaseontimemode.manager;

import com.shiyuan.sharingbaseontimemode.entity.OrderInfo;
import com.shiyuan.sharingbaseontimemode.mapper.OrderInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author MUSI
 * @Date 2022/7/2 3:58 PM
 * @Description
 * @Version
 **/
@Component
@RequiredArgsConstructor
public class OrderInfoManager {

    private final OrderInfoMapper orderInfoMapper;
    private final RedisManager redisManager;

    private static final AtomicInteger count = new AtomicInteger(0);

    private static final long time = 30 * 24 * 60 * 60 * 1000L;

    public void saveOrderInfo(){
        OrderInfo orderInfo = buildOrderInfo();
        orderInfoMapper.insertSelective(orderInfo);
    }

    public OrderInfo queryOrderInfo(Long orderId){
        OrderInfo cache = redisManager.getCache(orderId + "", OrderInfo.class);
        if (cache != null){
            return cache;
        }
        OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey(orderId);
        redisManager.setCache(orderId+"", orderInfo);
        return orderInfo;
    }

    public Object queryOrderList(String productName){
        return orderInfoMapper.queryList(productName);
    }

    public Object queryOrderInfoList(Long startId, Long endId){
        return orderInfoMapper.queryOrderInfoList(startId, endId);
    }

    private OrderInfo buildOrderInfo(){
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setId(nextOrderId());
        orderInfo.setOrderNumber(nextId()+"");
        orderInfo.setPrice(nextId());
        orderInfo.setProductName(nextId() + "");
        orderInfo.setProductId((int)System.currentTimeMillis());
        orderInfo.setUserId((int)System.currentTimeMillis());
        return orderInfo;
    }

    public static Long nextOrderId(){
        long changeTime = count.getAndAdd(1) * time;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = simpleDateFormat.format(new Date(System.currentTimeMillis() + changeTime));
        Random random = new Random();
        int i = Math.abs(random.nextInt(1000)) % 1000;
        return Long.parseLong(format + i);
    }

    public static Long nextId(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = simpleDateFormat.format(new Date(System.currentTimeMillis()));
        Random random = new Random();
        int i = Math.abs(random.nextInt(1000)) % 1000;
        return Long.parseLong(format + i);
    }
}
