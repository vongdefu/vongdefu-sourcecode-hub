package me.zeanzai.sharding.redis.bean.web;

import lombok.Data;
import me.zeanzai.sharding.redis.constant.ResponseCode;

/**
 * @author shawnwang
 */
@Data
public class DataResponse extends BaseResponse {
    /**
     * 响应值
     */
    private String code;
    /**
     * 查询对象
     */
    private Object data;
    /**
     * 响应信息
     */
    private String message;
    /**
     * 返回成功数据查询响应对象
     *
     * @param data 数据对象
     * @auther sunfeng
     */
    public static DataResponse success(Object data) {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setCode(ResponseCode.SUCCESS);
        dataResponse.setData(data);
        return dataResponse;
    }
    /**
     * 返回成功数据查询响应对象
     *
     * @param data 数据对象
     * @auther sunfeng
     */
    public static DataResponse success(Object data,String code) {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setCode(code);
        dataResponse.setData(data);
        return dataResponse;
    }

    /**
     * 返回异常数据查询响应对象
     *
     * @auther sunfeng
     */
    public static DataResponse error() {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setCode(ResponseCode.ERROR);
        return dataResponse;
    }
    /**
     * 返回异常数据查询响应对象
     *
     * @auther sunfeng
     */
    public static DataResponse error(String message) {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setCode(ResponseCode.ERROR);
        dataResponse.setMessage(message);
        return dataResponse;
    }
    /**
     * 返回异常数据查询响应对象
     *
     * @auther sunfeng
     */
    public static DataResponse error(String message,String code) {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setCode(code);
        dataResponse.setMessage(message);
        return dataResponse;
    }
    /**
     * 返回自定义数据查询响应对象
     *
     * @param code 响应值
     * @param data 查询对象
     * @auther sunfeng
     */
    public static DataResponse build(String code,Object data) {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setCode(code);
        dataResponse.setData(data);
        return dataResponse;
    }
}
