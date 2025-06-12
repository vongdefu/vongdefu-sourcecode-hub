package me.zeanzai.globalexception.config;

import me.zeanzai.globalexception.resolver.LanguageResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/23
 */
@Configuration
public class MessageConfig {

    @Bean
    public LocaleResolver localeResolver(){
        return new LanguageResolver();
    }

}
