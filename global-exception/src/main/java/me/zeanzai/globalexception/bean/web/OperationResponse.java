package me.zeanzai.globalexception.bean.web;

import lombok.Data;
import me.zeanzai.globalexception.common.constant.ResponseStatusCode;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/22
 */
@Data
public class OperationResponse extends BaseResponse{

    public static OperationResponse success(String message) {
        OperationResponse operationResponse = new OperationResponse();
        operationResponse.setCode(ResponseStatusCode.OK);
        operationResponse.setMessage(message);
        return operationResponse;
    }

    public static OperationResponse error(String message) {
        OperationResponse operationResponse = new OperationResponse();
        operationResponse.setCode(ResponseStatusCode.ERROR);
        operationResponse.setMessage(message);
        return operationResponse;
    }

    public static OperationResponse build(String code, String message) {
        OperationResponse operationResponse = new OperationResponse();
        operationResponse.setCode(code);
        operationResponse.setMessage(message);
        return operationResponse;
    }
}
