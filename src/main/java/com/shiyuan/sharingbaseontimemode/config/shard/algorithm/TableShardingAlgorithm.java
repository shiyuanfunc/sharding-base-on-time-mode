package com.shiyuan.sharingbaseontimemode.config.shard.algorithm;

import com.alibaba.fastjson.JSON;
import com.shiyuan.sharingbaseontimemode.utils.TableUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @Author MUSI
 * @Date 2022/7/2 3:00 PM
 * @Description
 * @Version 表的分片规则
 **/
@Slf4j
@Component
public class TableShardingAlgorithm implements PreciseShardingAlgorithm<Long> {

    @Override
    public String doSharding(Collection<String> availableTableNames, PreciseShardingValue<Long> shardingValue) {
        Long value = shardingValue.getValue();
        String logicTableName = shardingValue.getLogicTableName();
        log.warn("[TableShardingAlgorithm] 表分片key:{}, logic_table_name:{}, 可用的表:{}",
                value, logicTableName, JSON.toJSONString(availableTableNames));
        String tableSuffix = TableUtils.subTableName(value.toString());
        String tableName = logicTableName + "_" + tableSuffix;
        if (!availableTableNames.contains(tableName)) {
            ArrayList<String> availableTableNameList = new ArrayList<>(availableTableNames);
            tableName = availableTableNameList.get(0);
        }
        log.warn("[TableShardingAlgorithm] 当前选中的tableName:{}", tableName);
        return tableName;
    }
}
