package com.shiyuan.sharingbaseontimemode.config.shard;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author MUSI
 * @Date 2022/7/3 12:52 AM
 * @Description
 * @Version
 **/
@Data
@Component
@ConfigurationProperties(prefix = "spring.shard")
public class ShardConfig {
    private Map<String, DatasourceConfigProperties> dataSources;

    private Map<String, ShardTableConfigProperties> tables;
}
