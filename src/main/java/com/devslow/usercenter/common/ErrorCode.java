package com.devslow.usercenter.common;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 全局异常处理 错误码规范
 * @author slow
 * @CurrentTime 2024-7-14 16:50:55
 */
@AllArgsConstructor
public enum ErrorCode {
    SUCCESS(0,"ok",""),
    PARAMS_ERROR(40000,"请求参数错误",""),
    PARAMS_NULL_ERROR(40001,"请求数据为空",""),
    NOT_LOGIN(40100,"用户未登录",""),
    NO_AUTH_ERROR(40101,"无权限",""),
    SYSTEM_ERROR(50000,"系统内部异常","");
    /**
     * 错误码本身 枚举值使用final
     */
    private final int code;
    /**
     * 状态码信息
     */
    private final String message;
    /**
     * 状态码描述
     */
    private final String description;
//    设置返回错误状态码情况

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
