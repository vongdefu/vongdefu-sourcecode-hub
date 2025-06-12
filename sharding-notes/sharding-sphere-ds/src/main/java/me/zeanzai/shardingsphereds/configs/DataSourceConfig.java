package me.zeanzai.shardingsphereds.configs;

import lombok.Data;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/19
 */
@Data
public class DataSourceConfig {
    /**
     * 主机名
     */
    private String hostName;
    /**
     * 端口号
     */
    private int port;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
}
