package me.zeanzai.nacosasconfiger.config;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/21
 */
@Data
@AllArgsConstructor
public class MyGirlInfo {

    public String name;
    public Integer age;
    public List<String> address;
}
