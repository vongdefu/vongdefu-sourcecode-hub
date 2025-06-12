package me.zeanzai.seatatcccommon.utils;

import lombok.Data;

import java.io.Serializable;

/**
 * @author shawnwang
 */
@Data
public class ResponseResult<T> implements Serializable {
    private String code;
    private String msg;
    private T data;

    public static <T> ResponseResult<T> success(String code, String msg, T data) {
        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.setCode(code);
        responseResult.setMsg(msg);
        responseResult.setData(data);
        return responseResult;
    }
}
