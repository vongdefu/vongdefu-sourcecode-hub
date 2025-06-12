package me.zeanzai.globalexception.controller;

import me.zeanzai.globalexception.bean.user.UserDTO;
import me.zeanzai.globalexception.bean.web.DataResponse;
import me.zeanzai.globalexception.bean.web.OperationResponse;
import me.zeanzai.globalexception.common.constant.ResponseStatusCode;
import me.zeanzai.globalexception.exception.BizException;
import me.zeanzai.globalexception.util.MessageNacosUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/22
 */
@RestController
@RequestMapping("/test")
public class TestController {

    // 抛出异常信息
    @GetMapping("/throw01_01")
    public int test01_01() {
        int result;
        try {
            result = 1 / 0;
        } catch (BizException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @GetMapping("/throw01_02")
    public int test01_02() {
        Integer a = null;
        int b = a;
        return b;
    }

    @GetMapping("/throw1")
    public DataResponse test01() {
//        if ("0".equals("1")) {
//            throw GlobalExceptionUtil.bizException(ResponseStatusCode.EE4001);
//        }

        DataResponse dataResponse = new DataResponse();

        return dataResponse.error();
    }

    // 返回成功或失败

    // 返回list响应体

    // 返回page响应体

    // 返回操作响应结果


    // 直接抛出异常
    @GetMapping("/throw2_01")
    public OperationResponse test02_01() {

        if ("0".equals("0")) {
            throw new BizException(ResponseStatusCode.EE1001);
        }
        return OperationResponse.success(MessageNacosUtil.getExcptMsg(ResponseStatusCode.OK));
    }

    // 抛出校验异常
    @PostMapping("/throw2_02")
    public DataResponse<UserDTO> test02_02(
            @RequestBody @Validated UserDTO userDTO
    ) {
        return DataResponse.success(userDTO);
    }

    // 业务中组合单个异常信息
    @PostMapping("/throw2_03")
    public DataResponse<UserDTO> test02_03(
            @RequestBody @Validated UserDTO userDTO
    ) {
        if ("female".equals(userDTO.getSex())) {
            throw new BizException(ResponseStatusCode.EE3001, userDTO.getName());
        }
        return DataResponse.success(userDTO);
    }

    // 业务中组合多个异常信息
    @PostMapping("/throw2_04")
    public DataResponse<UserDTO> test02_04(
            @RequestBody @Validated UserDTO userDTO
    ) {
        if ("female".equals(userDTO.getSex()) && userDTO.getPhonenum()<138000) {
            throw new BizException(ResponseStatusCode.EE3002, userDTO.getName(), String.valueOf(userDTO.getPhonenum()));
        }
        return DataResponse.success(userDTO);
    }

    // 默认响应失败
    @PostMapping("/throw2_05")
    public DataResponse<UserDTO> test02_05(
            @RequestBody @Validated UserDTO userDTO
    ) {
        return DataResponse.error();

    }


}
