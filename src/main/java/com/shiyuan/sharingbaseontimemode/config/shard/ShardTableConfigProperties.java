package com.shiyuan.sharingbaseontimemode.config.shard;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author MUSI
 * @Date 2022/7/3 12:44 AM
 * @Description
 * @Version
 **/
@Data
public class ShardTableConfigProperties implements Serializable {

    private String logicTable;

    private String actualDataNodes;

    private String shardingColumns;
}
