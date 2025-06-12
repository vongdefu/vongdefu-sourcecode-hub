package me.zeanzai.shardingsphereds.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/19
 */
@Data
@Component
@ConfigurationProperties(prefix = "shardingstrategy")
@PropertySource("classpath:shardingstrategyConfig.properties") // 默认只能读取properties文件，如果读取yml文件，可以参考 https://blog.csdn.net/qq_26769513/article/details/88169054
public class ShardingStrategyConfig {
    /**
     * 原始库名，即分库分表前的库名
     */
    private String originDbName;
    /**
     * 要分的逻辑库总数
     */
    private int dbNum;
    /**
     * 每个逻辑库中表的数量
     */
    private int tableNumPerDb;
    /**
     * 库名的后缀
     */
    private String dbSuffix;
    /**
     * 表名后缀，一种分表分库维度对应一种后缀，比如C端维度：_by_user_id_  商户端维度：_by_merchant_id_
     */
    private List<String> tableSuffix;
    /**
     * 数据源
     */
    private List<DataSourceConfig> datasources;
}
