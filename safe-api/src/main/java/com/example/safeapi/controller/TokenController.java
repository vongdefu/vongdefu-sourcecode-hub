package com.example.safeapi.controller;


import com.example.safeapi.token.*;
import com.example.safeapi.util.ApiUtil;
import com.example.safeapi.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/api/token")
public class TokenController {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * API Token 属于 WebMvcConfiguration 中已经发行的接口，放行后，不需要经过 tokenInterceptor 拦截器 校验 。 所以只需要传入三个参数即可；
     * header 中传入： timestamp 和 sign timestamp: 时间戳，是客户端调用接口时对应的当前时间戳，时间戳用于防止DoS攻击。
     * 当黑客劫持了请求的url去DoS攻击，每次调用接口时接口都会判断服务器当前系统时间和接口中传的的timestamp的差值，
     * 如果这个差值超过某个设置的时间(假如5分钟)，那么这个请求将被拦截掉，如果在设置的超时时间范围内，是不能阻止DoS攻击的。
     * timestamp机制只能减轻DoS攻击的时间，缩短攻击时间。如果黑客修改了时间戳的值可通过sign签名机制来处理。
     * <p>
     * sign : 一般用于参数签名，防止参数被非法篡改，最常见的是修改金额等重要敏感参数，
     * sign的值一般是将所有非空参数按照升续排序然后+token+key+timestamp+nonce(随机数)拼接在一起，
     * 然后使用某种加密算法进行加密，作为接口中的一个参数sign来传递，也可以将sign放到请求头中。 接口在网络传输过程中如果被黑客挟持，并修改其中的参数值，然后再继续调用接口，
     * 虽然参数的值被修改了，但是因为黑客不知道sign是如何计算出来的，不知道sign都有哪些值构成， 不知道以怎样的顺序拼接在一起的，最重要的是不知道签名字符串中的key是什么，
     * 所以黑客可以篡改参数的值，但没法修改sign的值，当服务器调用接口前会按照sign的规则重新计算出sign的值然后和接口传递的sign参数的值做比较，
     * 如果相等表示参数值没有被篡改，如果不等，表示参数被非法篡改了，就不执行接口了。
     * <p>
     * body 中传入 appId : 1
     *
     * @param sign
     * @return
     */
    @PostMapping("/api_token")
    public ApiResponse<AccessToken> apiToken(String appId,
                                             @RequestHeader("timestamp") String timestamp,
                                             @RequestHeader("sign") String sign) {
        Assert.isTrue(!StringUtils.isEmpty(appId) && !StringUtils.isEmpty(timestamp) && !StringUtils.isEmpty(sign), "参数错误");
        log.info(appId + "    " + timestamp + "    " + sign);
        long reqeustInterval = System.currentTimeMillis() - Long.valueOf(timestamp);
        Assert.isTrue(reqeustInterval < 5 * 60 * 1000, "请求过期，请重新请求");

        // 1. 根据appId查询数据库获取appSecret
        //      客户端 申请调用 服务端 接口时，服务端会要求客户端先执行申请
        AppInfo appInfo = new AppInfo("1", "12345678954556");

        // 2. 校验签名
//        String signString = timestamp + appId + appInfo.getKey();
        Map<String, String> paramterMap = new HashMap<>();
        paramterMap.put("timestamp", timestamp);
        paramterMap.put("appid", appId);
        paramterMap.put("appKey", appInfo.getKey());
        String signString = ApiUtil.concatSignString(paramterMap);
        String signature = MD5Util.encode(signString);
        log.info(signature);
        Assert.isTrue(signature.equals(sign), "签名错误");

        // 3. 如果正确生成一个token保存到redis中，如果错误返回错误信息
        AccessToken accessToken = this.saveToken(0, appInfo, null);
        return ApiResponse.success(accessToken);
    }


    @NotRepeatSubmit(500000)
    @PostMapping("user_token")
    public ApiResponse<UserInfo> userToken(String username, String password) {
        // 根据用户名查询密码, 并比较密码(密码可以RSA加密一下)
        UserInfo userInfo = new UserInfo(username, "81255cb0dca1a5f304328a70ac85dcbd", "111111");
        String pwd = password + userInfo.getSalt();
        String passwordMD5 = MD5Util.encode(pwd);
        Assert.isTrue(passwordMD5.equals(userInfo.getPassword()), "密码错误");

        // 2. 保存Token
        AppInfo appInfo = new AppInfo("1", "12345678954556");
        AccessToken accessToken = this.saveToken(1, appInfo, userInfo);
        userInfo.setAccessToken(accessToken);
        return ApiResponse.success(userInfo);
    }

    private AccessToken saveToken(int tokenType, AppInfo appInfo, UserInfo userInfo) {
        String token = UUID.randomUUID().toString();
        // token有效期为2小时
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, 7200);
        Date expireTime = calendar.getTime();

        // 4. 保存token
        ValueOperations<String, TokenInfo> operations = redisTemplate.opsForValue();
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setTokenType(tokenType);
        tokenInfo.setAppInfo(appInfo);

        if (tokenType == 1) {
            tokenInfo.setUserInfo(userInfo);
        }

        operations.set(token, tokenInfo, 7200, TimeUnit.SECONDS);

        AccessToken accessToken = new AccessToken(token, expireTime);

        return accessToken;
    }

}
