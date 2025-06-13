package com.example.safeapi.token;

import com.example.safeapi.util.ApiUtil;
import com.example.safeapi.util.MD5Util;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


@Data
@Slf4j
public class ApiResponse<T> {

    /** 结果 */
    private ApiResult result;

    /** 数据 */
    private T data;

    /** 签名 */
    /**
     * 一般用于参数签名，防止参数被非法篡改，最常见的是修改金额等重要敏感参数，
     * sign的值一般是将所有非空参数按照升续排序然后+token+key+timestamp+nonce(随机数)拼接在一起，
     * 然后使用某种加密算法进行加密，作为接口中的一个参数sign来传递，也可以将sign放到请求头中。
     * 接口在网络传输过程中如果被黑客挟持，并修改其中的参数值，然后再继续调用接口，
     * 虽然参数的值被修改了，但是因为黑客不知道sign是如何计算出来的，不知道sign都有哪些值构成，
     * 不知道以怎样的顺序拼接在一起的，最重要的是不知道签名字符串中的key是什么，
     * 所以黑客可以篡改参数的值，但没法修改sign的值，当服务器调用接口前会按照sign的规则重新计算出sign的值然后和接口传递的sign参数的值做比较，
     * 如果相等表示参数值没有被篡改，如果不等，表示参数被非法篡改了，就不执行接口了。
     *
     * sign的作用是防止参数被篡改，客户端调用服务端时需要传递sign参数，
     * 服务器响应客户端时也可以返回一个sign用于客户端校验返回的值是否被非法篡改了。
     * 客户端传的sign和服务器端响应的sign算法可能会不同。
     *
     * 这里的 sign 的作用主要时防止 在服务端返回数据的过程中，参数被黑客劫持后篡改。
     * 客户端得到响应之后，利用和服务端约定的算法，重新计算签名，结果和返回的sign一致，就表明没有被篡改。
     */
    private String sign;


    public static <T> ApiResponse success(T data) {
        return response(ApiCodeEnum.SUCCESS.getCode(), ApiCodeEnum.SUCCESS.getMsg(), data);
    }

    public static ApiResponse error(String code, String msg) {
        return response(code, msg, null);
    }

    public static <T> ApiResponse response(String code, String msg, T data) {
        ApiResult result = new ApiResult(code, msg);
        ApiResponse response = new ApiResponse();
        response.setResult(result);
        response.setData(data);

        String sign = signData(data);
        response.setSign(sign);


        return response;
    }


    private static <T> String signData(T data) {
        // TODO 查询key
        String key = "12345678954556";
        Map<String, String> responseMap = null;
        try {
            responseMap = getFields(data);
        } catch (IllegalAccessException e) {
            return null;
        }
        String urlComponent = ApiUtil.concatSignString(responseMap);
        String signature = urlComponent + "key=" + key;
        String sign = MD5Util.encode(signature);

        return sign;
    }


    /**
     * @param data 反射的对象,获取对象的字段名和值
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static Map<String, String> getFields(Object data) throws IllegalAccessException, IllegalArgumentException {
        if (data == null) return null;
        Map<String, String> map = new HashMap<>();
        Field[] fields = data.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);

            String name = field.getName();
            Object value = field.get(data);
            if (field.get(data) != null) {
                map.put(name, value.toString());
            }
        }


        return map;
    }
}
