package com.shiyuan.sharingbaseontimemode.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author MUSI
 * @Date 2022/7/2 4:06 PM
 * @Description
 * @Version
 **/
@RestController
@RequestMapping(path = "/test")
public class TestController {

    @Autowired
    private OrderInfoManager orderInfoManager;
    @Autowired
    private ItemInfoManager itemInfoManager;


    @GetMapping(path = "/insert")
    public Object saveOrderInfo(){
        orderInfoManager.saveOrderInfo();
        return "success";
    }

    @GetMapping(path = "/query")
    public Object queryOrderInfo(Long orderId){
        return orderInfoManager.queryOrderInfo(orderId);
    }

    @GetMapping(path = "/query/name")
    public Object queryOrderInfoList(String productName){
        return orderInfoManager.queryOrderList(productName);
    }

    @GetMapping(path = "/query/range")
    public Object queryOrderList(Long startId, Long endId){
        return orderInfoManager.queryOrderInfoList(startId, endId);
    }

    @GetMapping(path = "/save/item")
    public Object saveItemInfo(){
        itemInfoManager.saveItemInfo();
        return "SUCCESS";
    }
}
