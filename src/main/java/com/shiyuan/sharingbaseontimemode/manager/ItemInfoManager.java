package com.shiyuan.sharingbaseontimemode.manager;

import com.shiyuan.sharingbaseontimemode.entity.ItemInfo;
import com.shiyuan.sharingbaseontimemode.mapper.ItemInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Random;

/**
 * @Author MUSI
 * @Date 2022/7/3 1:04 PM
 * @Description
 * @Version
 **/
@Component
@RequiredArgsConstructor
public class ItemInfoManager {


    private final ItemInfoMapper itemInfoMapper;

    public void saveItemInfo(){
        ItemInfo itemInfo = buildItemInfo();
        itemInfoMapper.insertSelective(itemInfo);
    }

    private ItemInfo buildItemInfo(){
        ItemInfo itemInfo = new ItemInfo();
        itemInfo.setCreateTime(new Date());
        itemInfo.setImgUrl(System.currentTimeMillis()+"");
        itemInfo.setName(System.currentTimeMillis()+"");
        itemInfo.setRemark(System.currentTimeMillis()+"");
        itemInfo.setUpdateTime(new Date());
        Random random = new Random();
        int randomInt = Math.abs(random.nextInt(10000));
        itemInfo.setTopicId(randomInt);
        return itemInfo;
    }
}
