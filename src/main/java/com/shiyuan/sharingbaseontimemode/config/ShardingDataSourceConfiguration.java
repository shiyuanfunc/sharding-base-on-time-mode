package com.shiyuan.sharingbaseontimemode.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceWrapper;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.shiyuan.sharingbaseontimemode.config.shard.DatasourceConfigProperties;
import com.shiyuan.sharingbaseontimemode.config.shard.ShardConfig;
import com.shiyuan.sharingbaseontimemode.config.shard.ShardTableConfigProperties;
import com.shiyuan.sharingbaseontimemode.config.shard.algorithm.ComplexKeysShardingAlgorithmImpl;
import com.shiyuan.sharingbaseontimemode.config.shard.algorithm.DatabaseShardingAlgorithm;
import com.shiyuan.sharingbaseontimemode.config.shard.algorithm.TableRangeShardingAlgorithm;
import com.shiyuan.sharingbaseontimemode.config.shard.algorithm.TableShardingAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.config.masterslave.MasterSlaveRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.ComplexShardingStrategyConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.MasterSlaveDataSourceFactory;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @Author MUSI
 * @Date 2022/7/2 2:46 PM
 * @Description
 * @Version sharding datasource 配置
 **/
@Slf4j
@Configuration
public class ShardingDataSourceConfiguration {

    @Value("${spring.datasource.sqlShow:true}")
    private String sqlShow;

    @Value("${spring.datasource.actualDataNodes}")
    private String actualDataNodes;
    @Autowired
    private TableRangeShardingAlgorithm tableRangeShardingAlgorithm;
    @Autowired
    private TableShardingAlgorithm tableShardingAlgorithm;
    @Autowired
    private DatabaseShardingAlgorithm databaseShardingAlgorithm;


    @Bean
    @ConditionalOnBean(ShardConfig.class)
    public DataSource shardingDataSource(ShardConfig shardConfig) throws SQLException {

        log.info("[TableRuleConfig] {}", JSON.toJSONString(shardConfig));
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(tableOrderInfoRuleConfiguration());
        Map<String, DataSource> dataSourceMap = createDataSourceMap(shardConfig);
        if (dataSourceMap.containsKey("default")) {
            shardingRuleConfig.setDefaultDataSourceName("default");
        }
        Properties properties = new Properties();
        properties.put("sql.show", sqlShow);
        // 配置主从时
        shardingRuleConfig.setMasterSlaveRuleConfigs(this.getMasterSlaveRuleConfigurations());
        return ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, properties);
    }
    private TableRuleConfiguration buildShardTableRule(Map.Entry<String, ShardTableConfigProperties> shardTableRuleEntry) {
        String tableName = shardTableRuleEntry.getKey();
        ShardTableConfigProperties shardTableConfigProperties = shardTableRuleEntry.getValue();
        TableRuleConfiguration tableRuleConfiguration = new TableRuleConfiguration(tableName, shardTableConfigProperties.getActualDataNodes());
        return tableRuleConfiguration;
    }

    public TableRuleConfiguration tableOrderInfoRuleConfiguration() {
        TableRuleConfiguration tableRuleConfiguration = new TableRuleConfiguration("order_info", actualDataNodes);
        tableRuleConfiguration.setDatabaseShardingStrategyConfig(
                new ComplexShardingStrategyConfiguration("id", new ComplexKeysShardingAlgorithmImpl()));

        tableRuleConfiguration.setTableShardingStrategyConfig(new StandardShardingStrategyConfiguration("id", tableShardingAlgorithm, tableRangeShardingAlgorithm));
        return tableRuleConfiguration;
    }

    public Map<String, DataSource> createDataSourceMap(ShardConfig shardConfig) {
        Map<String, DataSource> map = new HashMap<>();
        for (Map.Entry<String, DatasourceConfigProperties> datasourceConfigEntry : shardConfig.getDataSources().entrySet()) {
            String datasourceName = datasourceConfigEntry.getKey();
            DatasourceConfigProperties datasourceConfig = datasourceConfigEntry.getValue();
            DruidDataSourceWrapper dataSource = new DruidDataSourceWrapper();
            dataSource.setUsername(datasourceConfig.getUsername());
            dataSource.setPassword(datasourceConfig.getPassword());
            dataSource.setUrl(datasourceConfig.getUrl());
            dataSource.setDriverClassName(datasourceConfig.getDriverClass());
            dataSource.setMaxActive(datasourceConfig.getMaxActive());
            dataSource.setMinIdle(datasourceConfig.getMinIdle());
            dataSource.setInitialSize(datasourceConfig.getInitialSize());
            dataSource.setMaxWait(datasourceConfig.getMaxWait());
            dataSource.setTestWhileIdle(datasourceConfig.isTestWhileIdle());
            dataSource.setTestOnBorrow(datasourceConfig.isTestOnBorrow());
            dataSource.setTestOnReturn(datasourceConfig.isTestOnReturn());
            dataSource.setValidationQuery(datasourceConfig.getValidationQuery());
            dataSource.setValidationQueryTimeout(datasourceConfig.getValidationQueryTimeout());
            map.put(datasourceName, dataSource);
            log.warn("[init DataSource] name:{}, url:{}", datasourceName, datasourceConfig.getUrl());
        }
        return map;
    }


    private List<MasterSlaveRuleConfiguration> getMasterSlaveRuleConfigurations() {
        // 配置主从时 若该库/表涉及分表，第一个参数name 需要满足 actualDataNodes: shard_01.order_info_$->{2207..2212}此处的库名规则
        MasterSlaveRuleConfiguration masterSlaveRuleConfig1 = new MasterSlaveRuleConfiguration("shard_01",
                "shard_01_master", Collections.singletonList("shard_01_slave"));
//        MasterSlaveRuleConfiguration masterSlaveRuleConfig2 = new MasterSlaveRuleConfiguration("shard_02_ms",
//                "shard_02", Collections.singletonList("shard_02_slave"));
        return Lists.newArrayList(masterSlaveRuleConfig1);
    }

}
