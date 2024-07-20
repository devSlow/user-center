package com.devslow.usercenter.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类（优化返回对象）
 *
 * @param <T>
 * @author slow
 * @CurrentTime 2024-7-14 14:46:26
 */
@Data
@AllArgsConstructor
public class BaseResponse<T> implements Serializable {
    private int code;
    private T data;
    private String message;
    private String description;


    public BaseResponse(int code, T data, String description) {
        this.code = code;
        this.data = data;
        this.description = description;
    }

    public BaseResponse(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.description = errorCode.getDescription();
    }

}
