package me.zeanzai.nacosasconfiger.controller;

import me.zeanzai.nacosasconfiger.config.ENacosJsonDefinition;
import me.zeanzai.nacosasconfiger.config.MyBrotherInfo;
import me.zeanzai.nacosasconfiger.config.NacosConfigLocalCatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/7
 */
@RequestMapping("configer")
@RestController
public class GetPropertiesController {

    @GetMapping("/test")
    public String test(){
        return "hello configer!";
    }



//////////////////////////////////////////////////////////////////
    @Value("${spring.application.name}")
    private String applicationName;
    @GetMapping("/getServiceName")
    public String getServiceName(){
        return applicationName;
    }



//////////////////////////////////////////////////////////////////
    @Value("${spring.profiles.active}")
    private String profiles;
    @GetMapping("/profiles")
    public String profiles(){
        return profiles;
    }

//////////////////////////////////////////////////////////////////
    @Value("${mygirl.name}")
    private String mygirlname;

    @Value("${mygirl.sex}")
    private String mygirlsex;

    @Value("${mygirl.age}")
    private String mygirlage;
    @GetMapping("/mygirl")
    public String mygirl(){
        return "name: " + mygirlname + " sex: " + mygirlsex + " age: " + mygirlage;
    }

//////////////////////////////////////////////////////////////////
    @Autowired
    private MyBrotherInfo myBrotherInfo;

    /**
     * 获取配置中心配置项后转JavaBean进行获取
     * @return
     */
    @GetMapping("/mybrotherzinfo")
    public String mybrotherzinfo(){

        return "name: " + myBrotherInfo.getName() + " age: " + myBrotherInfo.getAge() + " sex: " + myBrotherInfo.getSex();
    }

    @Autowired
    private NacosConfigLocalCatch nacosConfigLocalCatch;
    @GetMapping("/mygirlsinfo")
    public List mygirlsinfo() {
        List localCatchConfig = nacosConfigLocalCatch.getLocalCatchConfig(ENacosJsonDefinition.MYGIRLS, List.class);
        return localCatchConfig;
    }


}
