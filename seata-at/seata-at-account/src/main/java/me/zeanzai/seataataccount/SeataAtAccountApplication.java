package me.zeanzai.seataataccount;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SeataAtAccountApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeataAtAccountApplication.class, args);
	}

}
