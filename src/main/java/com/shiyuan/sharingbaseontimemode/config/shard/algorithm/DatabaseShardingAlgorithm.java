package com.shiyuan.sharingbaseontimemode.config.shard.algorithm;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @Author MUSI
 * @Date 2022/7/2 2:56 PM
 * @Description
 * @Version
 *
 **/
@Component
@Slf4j
public class DatabaseShardingAlgorithm implements PreciseShardingAlgorithm<String>, RangeShardingAlgorithm<String> {

    @Override
    public String doSharding(Collection<String> availableDatabaseNames, PreciseShardingValue<String> shardingValue) {
        log.warn("[DatabaseShardingAlgorithm] 当前分片值:{}, 可用的数据库列表:{}", shardingValue, JSON.toJSONString(availableDatabaseNames));
        ArrayList<String> strings = new ArrayList<>(availableDatabaseNames);
        String database = strings.get(0);
        log.warn("[DatabaseShardingAlgorithm] 当前选中的database name:{}", database);
        return database;
    }

    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<String> rangeShardingValue) {
        log.warn("[DatabaseRangeShardingAlgorithm] 当前分片值:{}, 可用的数据库列表:{}", rangeShardingValue.getValueRange(), JSON.toJSONString(collection));
        return collection;
    }
}
