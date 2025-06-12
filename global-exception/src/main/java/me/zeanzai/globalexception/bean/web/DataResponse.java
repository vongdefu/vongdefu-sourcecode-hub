package me.zeanzai.globalexception.bean.web;

import lombok.Data;
import me.zeanzai.globalexception.common.constant.ResponseStatusCode;
import me.zeanzai.globalexception.util.MessageNacosUtil;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/22
 */
@Data
public class DataResponse<T> extends BaseResponse{

    private T data;

    public static <T> DataResponse response(String code, T data) {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setCode(code);
        dataResponse.setMessage(MessageNacosUtil.getExcptMsg(code));
        dataResponse.setData(data);
        return dataResponse;
    }

    public static <T> DataResponse response(String code, String msg, T data) {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setCode(code);
        dataResponse.setMessage(msg);
        dataResponse.setData(data);
        return dataResponse;
    }

    public static <T> DataResponse success(T data) {
        return response(ResponseStatusCode.OK, data);
    }

    public static DataResponse error(String code, String msg) {
        return response(code, msg, null);
    }

    public static DataResponse error() {
        return response(ResponseStatusCode.ERROR, null);
    }

}
