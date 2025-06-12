package me.zeanzai.sharding.redis.bean.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shawnwang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {

    /**
     * 异常码
     */
    protected String code;
    /**
     * 返回信息
     */
    private String message;
    /**
     * 返回数据
     */
    private Object data;

}

