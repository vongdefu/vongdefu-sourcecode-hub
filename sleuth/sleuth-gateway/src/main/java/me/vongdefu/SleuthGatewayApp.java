package me.vongdefu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SleuthGatewayApp {
    public static void main(String[] args) {
        SpringApplication.run(SleuthGatewayApp.class, args);
    }
}
