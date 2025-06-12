package me.zeanzai.sharding.sql.exception;

/**
 * @author shawnwang
 */
public class BaseException  extends RuntimeException {
    /**
     * 异常码
     */
    private String code;

    public BaseException() {
        super();
    }

    public BaseException(String message) {
        super(message);
    }
    public BaseException(String message, Throwable e) {
        super(message,e);
    }


    public BaseException(String methodName, String message){
        super(methodName+"|"+ message);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
