package me.zeanzai.nacosasconfiger.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/7
 */
@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "mybrother")// prefix 的值就是控制台添加的配置文件的名称
public class MyBrotherInfo {
    private String name;
    private String age;
    private String sex;
}

