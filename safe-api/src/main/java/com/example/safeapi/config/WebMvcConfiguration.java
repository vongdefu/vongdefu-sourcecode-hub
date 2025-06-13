package com.example.safeapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;


@Configuration
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    // 放行的 接口 ，也就是不需要 经过  tokenInterceptor 拦截器 校验 的接口。
    private static final String[] excludePathPatterns = {"/api/token/api_token"};

    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);

        // 符合【addPathPatterns】列表的请求路径，统一交给 tokenInterceptor 处理
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/api/**")
                .addPathPatterns("/test/**")
                .excludePathPatterns(excludePathPatterns);
    }
}


