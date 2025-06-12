package me.zeanzai.sharding.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching// 打开缓存
public class ShardingRedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShardingRedisApplication.class, args);
    }

}
