package com.shiyuan.sharingbaseontimemode.config.shard.algorithm;

import com.alibaba.fastjson.JSON;
import com.shiyuan.sharingbaseontimemode.utils.TableUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

/**
 * @Author MUSI
 * @Date 2022/7/2 10:15 PM
 * @Description
 * @Version 范围查询时的分片 处理
 **/
@Slf4j
@Component
public class TableRangeShardingAlgorithm implements RangeShardingAlgorithm<Long> {

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<Long> shardingValue) {

        Long begin = shardingValue.getValueRange().lowerEndpoint();
        Long end = shardingValue.getValueRange().upperEndpoint();
        String beginTableNum = TableUtils.subTableName(begin.toString());
        String endTableNum = TableUtils.subTableName(end.toString());
        String logicTableName = shardingValue.getLogicTableName();
        List<String> targetTables = getMonthBetween(beginTableNum, endTableNum, logicTableName + "_");
        log.warn("[TableRangeShardingAlgorithm] 范围选中的分表名字:{}", JSON.toJSONString(targetTables));
        return targetTables;
    }

    public static List<String> getMonthBetween(String minDate, String maxDate, String prefix) {
        ArrayList<String> result = new ArrayList<>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyMM");
            Calendar min = Calendar.getInstance();
            Calendar max = Calendar.getInstance();
            min.setTime(sdf.parse(minDate));
            min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
            max.setTime(sdf.parse(maxDate));
            max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
            Calendar curr = min;
            while (curr.before(max)) {
                result.add(prefix + sdf.format(curr.getTime()));
                curr.add(Calendar.MONTH, 1);
            }
        } catch (ParseException ignored) {
            log.error("[TableRangeShardingAlgorithm] parse error, startDate:{}, endDate:{}", minDate, maxDate);
        }
        return result;
    }
}
