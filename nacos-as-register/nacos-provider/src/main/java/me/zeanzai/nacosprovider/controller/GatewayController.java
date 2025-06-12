package me.zeanzai.nacosprovider.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shawnwang
 */
@RestController
@RequestMapping("providerbygateway")
public class GatewayController {

    @GetMapping("/hello")
    public String hello(){
        return "hello, provider!!!";
    }
}
