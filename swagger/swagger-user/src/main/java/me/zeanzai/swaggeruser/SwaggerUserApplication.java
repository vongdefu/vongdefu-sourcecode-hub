package me.zeanzai.swaggeruser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class SwaggerUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwaggerUserApplication.class, args);
    }

}
