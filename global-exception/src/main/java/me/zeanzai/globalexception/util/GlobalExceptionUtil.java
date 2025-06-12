//package me.zeanzai.globalexception.util;
//
//import me.zeanzai.globalexception.bean.web.BaseResponse;
//import me.zeanzai.globalexception.exception.BizException;
//import org.springframework.stereotype.Component;
//
///**
// * @author shawnwang
// * @version 1.0
// * @describe
// * @date 2023/4/22
// */
//@Component
//public class GlobalExceptionUtil {
//
//    public static BizException bizException(String excptCode) {
//        return new BizException(createResult(excptCode));
//    }
//
//    public static BizException bizException(String excptCode, String... args) {
//        return new BizException(createResult(excptCode, args));
//    }
//
//    private static BaseResponse createResult(String excptCode) {
//        return new BaseResponse(excptCode, getExcptMsg(excptCode));
//    }
//
//    private static BaseResponse createResult(String excptCode, String excptMsg) {
//        return new BaseResponse(excptCode, excptMsg);
//    }
//
//    private static BaseResponse createResult(String excptCode, String... args) {
//        return new BaseResponse(excptCode, getExcptMsg(excptCode, args));
//    }
//
//    private static String getExcptMsg(String excptCode) {
//        return MessageNacosUtil.getExcptMsg(excptCode);
//    }
//
//    private static String getExcptMsg(String excptCode, String... args) {
//        return MessageNacosUtil.getExcptMsg(excptCode, args);
//    }
//
//}
