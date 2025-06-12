package me.zeanzai.nacosprovider.controller;

import me.zeanzai.nacosprovider.entity.MyFiles;
import me.zeanzai.nacosprovider.entity.Student;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.Charsets;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@RestController
@RequestMapping("provider")
@Slf4j
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        return "hello, provider!!!";
    }

    /**
     * 传递多个参数
     * @param name
     * @param age
     * @return
     */
    @PostMapping("/test01")
    public String test01(String name, String age) {
        return "name: " + name + " age: " + age;
    }

    /**
     * URL中携带参数
     * @param name
     * @param age
     * @return
     */
    @GetMapping("/test02/{name}/{age}")
    public String test02(@PathVariable("name") String name, @PathVariable("age")Integer age) {
        return "name: " + name + ", age: " + age.toString();
    }

    /**
     * 传递对象
     * @param student
     */
    @PostMapping("/test03")
    public void test03(@RequestBody Student student) throws InterruptedException {
        Thread.sleep(3000);
        log.info(student.toString());
    }

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping(value = "/test04", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String test04(@RequestPart("file") MultipartFile file) {
        return file.getOriginalFilename();
    }

    /**
     * 文件下载
     * @param response
     * @param myFiles
     */
    @PostMapping(value = "/test05", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void test05(@RequestBody MyFiles myFiles, HttpServletResponse response) {

        String filepath = myFiles.getPath()+"/"+myFiles.getFileName();

        response.setContentType("application/x-download;charset=" + Charsets.UTF_8.displayName());
        response.addHeader("Content-Disposition",
                "attachment;filename=" + myFiles.getFileName());

        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(filepath);
            OutputStream    out             = response.getOutputStream();
            byte            buffer[]        = new byte[1024];
            int length = 0;
            while ((length = fileInputStream.read(buffer)) >= 0){
                out.write(buffer,0,length);
            }

            ServletOutputStream outputStream = response.getOutputStream();
            IOUtils.copy(fileInputStream, outputStream);
            outputStream.flush();
        } catch (IOException e) {
            log.info("下载失败....");
        } finally {
            if(fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


}
