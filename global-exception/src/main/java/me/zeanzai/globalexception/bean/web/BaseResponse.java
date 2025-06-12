package me.zeanzai.globalexception.bean.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse{
    protected String code;
    private String message;
}
