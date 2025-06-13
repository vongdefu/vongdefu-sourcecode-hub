package com.example.safeapi.controller;

import com.example.safeapi.token.ApiResponse;
import com.example.safeapi.token.NotRepeatSubmit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/5
 */
@Slf4j
@RestController
@RequestMapping("/test/")
public class TestController {

    @GetMapping("test")
    @NotRepeatSubmit(10000)
    public ApiResponse<String> test(){
        return ApiResponse.success("sss");
    }
}
