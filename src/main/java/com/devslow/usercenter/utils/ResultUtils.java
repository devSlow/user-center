package com.devslow.usercenter.utils;

import com.devslow.usercenter.common.BaseResponse;
import com.devslow.usercenter.common.ErrorCode;

/**
 * 返回工具类（创建返回对象）
 *
 * @author slow
 * @CurrentTime 2024-7-14 19:27:13
 */
public class ResultUtils {
    /**
     * 成功
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok", "");
    }

    /**
     * 失败
     * @param errorCode
     * @return
     */


    public static BaseResponse error(ErrorCode errorCode, String messagge, String description) {
        return new BaseResponse<>(errorCode.getCode(),null, messagge, description);
    }

    public static BaseResponse error(ErrorCode errorCode, String description) {
        return new BaseResponse<>(errorCode.getCode(), errorCode.getMessage(), description);
    }

    public static BaseResponse error(int code, String message, String description) {
        return new BaseResponse<>(code,null, message, description);
    }
}
