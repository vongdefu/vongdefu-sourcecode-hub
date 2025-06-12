package me.zeanzai.seatatccaccount;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SeataTccAccountApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeataTccAccountApplication.class, args);
    }

}
