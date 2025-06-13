package com.example.safeapi;

import com.example.safeapi.util.ApiUtil;
import com.example.safeapi.util.MD5Util;

import java.util.HashMap;
import java.util.Map;

public class MockClient {

    /**
     * 不需要登录就可以访问的接口。
     */
    private static void unrequireLogin(){
        // 模拟 不需要登录 就可以访问的接口所需的参数
        long timestamp = System.currentTimeMillis();
        String appid = "1";
        String appKey = "12345678954556";   // appKey 客户端应用在申请调用服务端应用时，服务端应用给客户端应用分配的；

        Map<String, String> paramterMap = new HashMap<>();
        paramterMap.put("timestamp", String.valueOf(timestamp));
        paramterMap.put("appid", appid);
        paramterMap.put("appKey", appKey);
        String signString = ApiUtil.concatSignString(paramterMap);
        String sign = MD5Util.encode(signString);

        System.out.println("### 不需要登录的接口");
        System.out.println("POST http://localhost:9003/api/token/api_token");
        System.out.println("Content-Type: application/x-www-form-urlencoded");
        System.out.println("timestamp: "+timestamp);
        System.out.println("sign: "+sign);
        System.out.println();
        System.out.println("appId = "+appid);
    }

    /**
     * 登录接口
     * @param accessToken
     */
    private static void login(String accessToken){

        long timestamp = System.currentTimeMillis();
        String username="1";
        String password = "123456";
        String nonce = "A1scr6";
        String token = accessToken;      // 这是 /api_token 接口中返回的 token；

        String appKey = "12345678954556";   // appKey 客户端应用在申请调用服务端应用时，服务端应用给客户端应用分配的；


        Map<String, String> paramterMap = new HashMap<>();
        paramterMap.put("username", username);
        paramterMap.put("password", password);
        // 登录接口，在被拦截器处理的时候，实际上调用的是 ApiUtil.concatSignString(HttpServletRequest request)
        // 这里不太好模拟 HttpServletRequest ，所以直接使用 map 的方式， 效果一样。
        String signString = ApiUtil.concatSignString(paramterMap) + appKey + token + timestamp + nonce;
        String sign = MD5Util.encode(signString);

        System.out.println("### 登录接口");
        System.out.println("POST http://localhost:9003/api/token/user_token");
        System.out.println("Content-Type: application/x-www-form-urlencoded");
        System.out.println("nonce: "+nonce);
        System.out.println("timestamp: "+timestamp);
        System.out.println("token: "+token);
        System.out.println("sign: "+sign);
        System.out.println();
        System.out.println("username = " + username + " &");
        System.out.println("password = "+password);
    }

    /**
     * 不带请求参数的业务接口
     *
     * @param utoken
     */
    private static void shouldLogin(String utoken){

        long timestamp = System.currentTimeMillis();
        String nonce = "A1scr6";
        String token = utoken;      // 这是 /user_token 接口中返回的 token；
        String appKey = "12345678954556";   // appKey 客户端应用在申请调用服务端应用时，服务端应用给客户端应用分配的；

        String signString = appKey + token + timestamp + nonce;
        String sign = MD5Util.encode(signString);

        System.out.println("### 需要登录之后才可以访问的接口");
        System.out.println("GET http://localhost:9003/test/test");
        System.out.println("Content-Type: application/x-www-form-urlencoded");
        System.out.println("nonce: "+nonce);
        System.out.println("timestamp: "+timestamp);
        System.out.println("token: "+token);
        System.out.println("sign: "+sign);
        System.out.println();


    }

    public static void main(String[] args) {

//        unrequireLogin();
//        login("75daa32d-b300-4fb0-abf0-70e0be097b3f");
        shouldLogin("65014ed6-8208-409d-81a0-a612c234ca93");
    }
}
