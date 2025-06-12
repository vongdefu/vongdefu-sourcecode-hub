package me.zeanzai.shardingmultids.context.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shawnwang
 */
@Configuration
//@PropertySource("classpath:datasources.properties")
@ConfigurationProperties(prefix = "spring")
public class DataSources {
    /**
     * 数据源01信息
     */
    private DataSourceConfig datasource01;
    /**
     * 数据源02信息
     */
    private DataSourceConfig datasource02;
    /**
     * 数据源03信息
     */
    private DataSourceConfig datasource03;
    /**
     * 数据源集合
     */
    private static List<DataSource> dataSources = new ArrayList<DataSource>();


    @Bean
    public List<DataSource> ds() throws Exception {
        dataSources.add(buildDruidDataSource(datasource01));
        dataSources.add(buildDruidDataSource(datasource02));
        dataSources.add(buildDruidDataSource(datasource03));
        return dataSources;
    }

    /**
     * 构建Druid数据源
     */
    public DruidDataSource buildDruidDataSource(DataSourceConfig dataSourceConfig) {
        return DataSourceBuilder.create()
                .type(DruidDataSource.class)
                .driverClassName(dataSourceConfig.getDriverClassName())
                .url(dataSourceConfig.getUrl())
                .username(dataSourceConfig.getUsername())
                .password(dataSourceConfig.getPassword())
                .build();
    }

    @Bean
    @Primary
    public DataSourceRouter routingDataSource() throws Exception {
        Map<Object, Object> targetDataSources = new HashMap<>();

        List<DataSource> dataSources = ds();
        targetDataSources.put(DataSourceContextHolder.DATA_SOURCE_01, dataSources.get(0));
        targetDataSources.put(DataSourceContextHolder.DATA_SOURCE_02, dataSources.get(1));
        targetDataSources.put(DataSourceContextHolder.DATA_SOURCE_03, dataSources.get(2));

        DataSourceRouter routingDataSource = new DataSourceRouter();
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(dataSources.get(0));
        return routingDataSource;
    }

    /**
     * 设置事务，事务需要知道当前使用的是哪个数据源才能进行事务处理
     *
     * @return
     */
    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager() throws Exception {
        return new DataSourceTransactionManager(routingDataSource());
    }

    /**
     * 获取数据源数量
     * @return 数据源数量
     */
    public static int getDataSourceSize() {
        return dataSources.size();
    }

    public DataSourceConfig getDatasource01() {
        return datasource01;
    }

    public void setDatasource01(DataSourceConfig datasource01) {
        this.datasource01 = datasource01;
    }

    public DataSourceConfig getDatasource02() {
        return datasource02;
    }

    public void setDatasource02(DataSourceConfig datasource02) {
        this.datasource02 = datasource02;
    }

    public DataSourceConfig getDatasource03() {
        return datasource03;
    }

    public void setDatasource03(DataSourceConfig datasource03) {
        this.datasource03 = datasource03;
    }

}


