package com.shiyuan.sharingbaseontimemode.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceWrapper;
import com.alibaba.fastjson.JSON;
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
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
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

    @Value("${spring.datasource.username:root}")
    private String username;

    @Value("${spring.datasource.password:mysqlroot}")
    private String password;
    @Value("${spring.datasource.url}")
    private String jdbcUrl;

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

       // MasterSlaveRuleConfiguration masterSlaveRuleConfiguration = new MasterSlaveRuleConfiguration();

        System.out.println("[TableRuleConfig] " + JSON.toJSONString(shardConfig));
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(tableOrderInfoRuleConfiguration());
        Map<String, DataSource> dataSourceMap = createDataSourceMap(shardConfig);
        if (dataSourceMap.containsKey("default")){
            shardingRuleConfig.setDefaultDataSourceName("default");
        }
        Properties properties = new Properties();
        properties.put("sql.show", sqlShow);
        return ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, properties);
    }

    private TableRuleConfiguration buildShardTableRule(Map.Entry<String, ShardTableConfigProperties> shardTableRuleEntry){
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

    private DataSource createDataSource(String jdbcUrl) {
        DruidDataSourceWrapper druidDataSourceWrapper = new DruidDataSourceWrapper();
        druidDataSourceWrapper.setUrl(jdbcUrl);
        druidDataSourceWrapper.setUsername(username);
        druidDataSourceWrapper.setPassword(password);
        druidDataSourceWrapper.setDriverClassName(com.mysql.cj.jdbc.Driver.class.getName());
        return druidDataSourceWrapper;
    }

}
