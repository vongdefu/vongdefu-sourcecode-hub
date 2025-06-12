package me.zeanzai.seatatccorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("me.zeanzai.seatatccorder.feign")
public class SeataTccOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeataTccOrderApplication.class, args);
    }

}
