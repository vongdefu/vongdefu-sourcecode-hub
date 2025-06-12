package me.zeanzai.shardingsaas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ShardingSaasApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShardingSaasApplication.class, args);
    }

}
