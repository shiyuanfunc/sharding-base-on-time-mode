package com.shiyuan.sharingbaseontimemode.config.shard;

import com.mysql.cj.jdbc.Driver;
import lombok.Data;

/**
 * @Author MUSI
 * @Date 2022/7/3 2:05 PM
 * @Description
 * @Version
 **/
@Data
public class DatasourceConfigProperties {

    private String username;
    private String password;
    private String url;
    protected volatile String driverClass = Driver.class.getName();
    protected volatile int initialSize = 0;
    protected volatile int maxActive = 8;
    protected volatile int minIdle = 0;
    protected volatile int maxIdle = 8;
    protected volatile long maxWait = -1L;
    protected volatile boolean testOnBorrow;
    protected volatile boolean testOnReturn;
    protected volatile boolean testWhileIdle;
    protected volatile String validationQuery = "SELECT 1";
    protected volatile int validationQueryTimeout = 2000;
    protected volatile boolean poolPreparedStatements;
    protected volatile boolean sharePreparedStatements;
    protected volatile int maxPoolPreparedStatementPerConnectionSize;
}
