package com.shiyuan.sharingbaseontimemode.config.shard.algorithm;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @Author MUSI
 * @Date 2022/7/2 9:39 PM
 * @Description
 * @Version
 **/
@Slf4j
public class ComplexKeysShardingAlgorithmImpl implements ComplexKeysShardingAlgorithm<Long> {

    /**
     * 如果返回一个集合，则会依次对每个库/表进行处理 （插入会每个库/表都写一条数据）
     * @param collection available data sources or tables's names
     * @param complexKeysShardingValue sharding value
     * @return
     */
    @Override
    public Collection<String> doSharding(Collection<String> collection, ComplexKeysShardingValue<Long> complexKeysShardingValue) {
        log.warn("[ComplexKeysShardingAlgorithmImpl] [collection] : {}", JSON.toJSONString(collection));
        log.warn("[ComplexKeysShardingAlgorithmImpl] [complexKeysShardingValue] : {}", JSON.toJSONString(complexKeysShardingValue));
        List<String> list = new ArrayList<>(collection);
        List<String> result = Collections.singletonList(list.get(0));
        log.warn("[ComplexKeysShardingAlgorithmImpl] 选中的结果:{}", JSON.toJSONString(result));
        return result;
    }
}
