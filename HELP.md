
## sharding jdbc分表
### 利用sharding jdbc 基于时间的水平分表
> 按月维度进行分表, 可设置多库。
> 
#### 数据源配置
```java
com.shiyuan.sharingbaseontimemode.config.ShardingDataSourceConfiguration.shardingDataSource
```
#### 分库分表规则算法实现
```java
com.shiyuan.sharingbaseontimemode.config.shard.algorithm
```