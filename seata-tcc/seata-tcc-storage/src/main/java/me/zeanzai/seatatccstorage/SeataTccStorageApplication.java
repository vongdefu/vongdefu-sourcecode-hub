package me.zeanzai.seatatccstorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SeataTccStorageApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeataTccStorageApplication.class, args);
    }

}
