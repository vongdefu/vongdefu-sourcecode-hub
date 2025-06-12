package me.zeanzai.shardingmultids.context.config;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shawnwang
 */
@Data
public class DataSourceConfig {
    /**
     * driverClassName
     */
    private String driverClassName;
    /**
     * url
     */
    private String url;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
}

