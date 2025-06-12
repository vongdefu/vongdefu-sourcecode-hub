package me.zeanzai.sharding.sql.bean.web;

import lombok.Data;
import me.zeanzai.sharding.sql.constant.ResponseCode;

/**
 * @author shawnwang
 */
@Data
public class OperationResponse extends BaseResponse {
    /**
     * 响应值
     */
    private  String code;
    /**
     * 响应信息
     */
    private String message;

    /**
     * 返回成功操作响应对象
     *
     * @param message 提示信息
     * @auther sunfeng
     */
    public static OperationResponse success(String message) {
        OperationResponse operationResponse = new OperationResponse();
        operationResponse.setCode(ResponseCode.SUCCESS);
        operationResponse.setMessage(message);
        return operationResponse;
    }

    /**
     * 返回异常操作响应对象
     *
     * @param message 提示信息
     * @auther sunfeng
     */
    public static OperationResponse error(String message) {
        OperationResponse operationResponse = new OperationResponse();
        operationResponse.setCode(ResponseCode.ERROR);
        operationResponse.setMessage(message);
        return operationResponse;
    }

    /**
     * 返回自定义操作响应对象
     *
     * @param code 响应值
     * @param message 响应信息
     * @auther sunfeng
     */
    public static OperationResponse build(String code,String message) {
        OperationResponse operationResponse = new OperationResponse();
        operationResponse.setCode(code);
        operationResponse.setMessage(message);
        return operationResponse;
    }
}
