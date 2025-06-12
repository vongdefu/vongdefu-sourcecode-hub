package me.zeanzai.sharding.datasource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@MapperScan(basePackages = "me.zeanzai.sharding.datasource.mapper")
public class ShardingDatasourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShardingDatasourceApplication.class, args);
    }

}
