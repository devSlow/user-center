package com.devslow.usercenter.exception;

import com.devslow.usercenter.common.ErrorCode;

/**
 * 自定义异常类 （业务异常）
 * 相对于JAVA异常，有了更多字段
 * @author slow
 * @CurrentTime 2024-7-14 19:47:45
 */
public class BusinessException extends RuntimeException {
    //    不需要set 就定义为final
    private final int code;
    private final String description;

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public BusinessException(ErrorCode errorCode, String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

}
