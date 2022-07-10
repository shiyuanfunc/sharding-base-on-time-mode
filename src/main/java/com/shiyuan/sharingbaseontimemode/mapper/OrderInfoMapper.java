package com.shiyuan.sharingbaseontimemode.mapper;

import com.shiyuan.sharingbaseontimemode.entity.OrderInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderInfo record);

    int insertSelective(OrderInfo record);

    OrderInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderInfo record);

    int updateByPrimaryKey(OrderInfo record);

    List<OrderInfo> queryList(@Param("productName") String productName);

    List<OrderInfo> queryOrderInfoList(@Param("startId") Long startId, @Param("endId") Long endId);
}