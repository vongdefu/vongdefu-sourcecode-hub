package me.zeanzai.nacosasconfiger.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/7
 */
@RequestMapping("/tradition")
@RestController
public class TraditionalGetPropertiesController {

    @Value("${traditional.name}")
    private String name;


    @Value("${traditional.sex}")
    private String sex;


    @Value("${traditional.age}")
    private String age;

    @GetMapping("/myinfo")
    public String myinfo(){
        return "name: " + name + " sex: " + sex + " age: " + age;
    }


}
