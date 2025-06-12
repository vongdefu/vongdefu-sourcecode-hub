package me.zeanzai.globalexception.exception;

import lombok.Data;
import me.zeanzai.globalexception.bean.web.BaseResponse;
import me.zeanzai.globalexception.util.MessageNacosUtil;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/22
 */
@Data
public class BizException extends BaseException{

    private BaseResponse baseResponse = new BaseResponse();

    public BizException(BaseResponse baseResponse) {
        super(baseResponse.getCode()+", "+ baseResponse.getMessage());
        this.baseResponse = baseResponse;
    }

    public BizException(BaseResponse baseResponse, Throwable throwable) {
        super(baseResponse.getCode() + ", " + baseResponse.getMessage(), throwable);
        this.baseResponse = baseResponse;
    }

    public BizException(String code, String... msg) {
        super(code+ ", "+msg);
        this.baseResponse.setCode(code);
        String excptMsg = MessageNacosUtil.getExcptMsg(code, msg);
        this.baseResponse.setMessage(excptMsg);
    }


    public BizException(String code, String msg, Throwable throwable) {
        super(code+ ", "+msg, throwable);
        this.baseResponse.setCode(code);
        this.baseResponse.setMessage(msg);
    }

}

