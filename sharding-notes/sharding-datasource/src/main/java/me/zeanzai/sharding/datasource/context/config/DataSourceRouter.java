package me.zeanzai.sharding.datasource.context.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author shawnwang
 */
@Slf4j
public class DataSourceRouter extends AbstractRoutingDataSource {


    @Override
    protected Object determineCurrentLookupKey() {
        log.info("当前数据源为"+DataSourceContextHolder.getDataSourceType());
        //返回选择的数据源
        return DataSourceContextHolder.getDataSourceType();
    }
}
