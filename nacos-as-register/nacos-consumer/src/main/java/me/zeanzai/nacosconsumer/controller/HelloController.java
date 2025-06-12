package me.zeanzai.nacosconsumer.controller;

import me.zeanzai.nacosconsumer.entity.MyFiles;
import me.zeanzai.nacosconsumer.entity.Student;
import me.zeanzai.nacosconsumer.service.ProviderService;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.Charsets;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@RestController
@RequestMapping("consumer")
@Slf4j
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        return "hello, consumer!!!";
    }


    /**
     * 注入OpenFeign的接口
     */
    @Autowired
    private ProviderService providerService;

    @GetMapping("/hello2")
    public String hello2(){
        return providerService.hello();
    }

    @Value("${service-url.nacos-provider}")
    private String serviceUrl;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/hello1")
    public String getHelloByRestTemplate(){
        ResponseEntity<String> forEntity = restTemplate.getForEntity(serviceUrl + "/provider/hello", String.class);
        return forEntity.getBody();
    }

    /**
     * 传递多个参数
     * @param name
     * @param age
     * @return
     */
    @PostMapping("/test01")
    public String test01(String name, String age) {
        return providerService.test01(name, age);
    }

    /**
     * URL 中携带多个参数
     * @param name
     * @param age
     * @return
     */
    @PostMapping("/test02/{name}/{age}")
    public String test01(@PathVariable("name") String name, @PathVariable("age")Integer age) {
        return providerService.test02(name, age);
    }

    /**
     * 传递对象
     */
    @PostMapping("/test03")
    public void test03() {
        Student student = new Student();
        student.setName("laowang");
        student.setAge(13);
        providerService.test03(student);
    }

    /**
     * 上传文件
     * @param file
     * @return
     */
    @PostMapping(value = "/test04")
    public String test04(MultipartFile file) {
        return providerService.test04(file);
    }

    /**
     * 下载
     * @param myFiles
     * @return
     */
    @PostMapping("/test05")
    public void test05(@RequestBody MyFiles myFiles, HttpServletResponse httpServletResponse) {
        String filename = myFiles.getPath() + "/" + myFiles.getFileName();
        httpServletResponse
                .setContentType("application/x-download;charset=" + Charsets.UTF_8.displayName());
        httpServletResponse.addHeader("Content-Disposition",
                "attachment;filename=" + myFiles.getFileName());
        Response response = providerService.test05(myFiles);
        Response.Body body = response.body();
        try (OutputStream outputStream = httpServletResponse.getOutputStream();
             InputStream inputStream = body.asInputStream()) {
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
        } catch (IOException e) {
            log.error("下载文件异常 filepath:[{}]", filename, e);
        }
    }


}
