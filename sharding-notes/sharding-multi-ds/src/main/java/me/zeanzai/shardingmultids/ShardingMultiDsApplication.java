package me.zeanzai.shardingmultids;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ShardingMultiDsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShardingMultiDsApplication.class, args);
    }

}
