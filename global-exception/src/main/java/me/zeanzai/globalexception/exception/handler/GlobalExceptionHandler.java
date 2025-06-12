package me.zeanzai.globalexception.exception.handler;

import me.zeanzai.globalexception.bean.web.BaseResponse;
import me.zeanzai.globalexception.exception.BizException;
import me.zeanzai.globalexception.util.MessageNacosUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/22
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BizException.class)
    public ResponseEntity bizExceptionHandler(BizException bizException) {
        BaseResponse baseResponse = bizException.getBaseResponse();
        return new ResponseEntity(baseResponse, HttpStatus.OK);
    }


    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException methodArgumentNotValidException) {
        BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
        if (bindingResult.getFieldErrors() != null) {
            // 如果校验结果中有多个异常，就只返回第一个异常信息
            String excptCode = bindingResult.getFieldErrors().get(0).getDefaultMessage();
            String excptMsg = MessageNacosUtil.getExcptMsg(excptCode);
            BizException bizException = new BizException(excptCode, excptMsg);
            return new ResponseEntity(bizException.getBaseResponse(), HttpStatus.OK);
        }
        return new ResponseEntity(null, HttpStatus.OK);
    }
}
