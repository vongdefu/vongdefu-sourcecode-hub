package me.zeanzai.gatewaydemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient  // 添加服务注册与发现的注解
@SpringBootApplication
public class GatewayDemoApplication {

    public static void main(String[] args) {
//        // 配置线程组
//        System.setProperty("reactor.netty.ioSelectCount","1");
//        // 这里工作线程数为2-4倍都可以。看具体情况
//        int ioWorkerCount = Math.max(Runtime.getRuntime().availableProcessors()*3, 4);
//        System.setProperty("reactor.netty.ioWorkerCount",String.valueOf(ioWorkerCount));
        SpringApplication.run(GatewayDemoApplication.class, args);
    }

}
