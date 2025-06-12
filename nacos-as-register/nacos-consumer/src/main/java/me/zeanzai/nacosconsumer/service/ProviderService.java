package me.zeanzai.nacosconsumer.service;

import me.zeanzai.nacosconsumer.entity.MyFiles;
import me.zeanzai.nacosconsumer.entity.Student;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(value = "nacos-provider")
public interface ProviderService {

    @RequestMapping("/provider/hello")
    String hello();

    @PostMapping("/provider/test01")
    String test01(@RequestParam("name")String name, @RequestParam("age")String age);

    @GetMapping("/provider/test02/{name}/{age}")
    String test02(@PathVariable("name") String name, @PathVariable("age")Integer age);

    @PostMapping("/provider/test03")
    void test03(@RequestBody Student student);

    @PostMapping(value = "/provider/test04",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String test04(@RequestPart("file") MultipartFile file);

    @PostMapping(value = "/provider/test05", consumes = MediaType.APPLICATION_JSON_VALUE)
    Response test05(@RequestBody MyFiles myFiles);

}
