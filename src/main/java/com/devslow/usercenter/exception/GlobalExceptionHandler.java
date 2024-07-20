package com.devslow.usercenter.exception;

import com.devslow.usercenter.common.BaseResponse;
import com.devslow.usercenter.common.ErrorCode;
import com.devslow.usercenter.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器（整个项目的全局异常，服务器异常及业务异常）
 * @author slow
 * @CurrentTime 2024-7-14 20:21:42
 */
@RestControllerAdvice //aop 在调用方法前后提供额外处理
@Slf4j  //日志对象
public class GlobalExceptionHandler {

    /**
     * businessExceptionHandler只去捕获 BusinessException
     * @param e
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e) {
        log.error("businessException:"+e.getMessage(), e);
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(Exception.class)
    public BaseResponse runtimeExceptionHandler(BusinessException e) {
        log.error("runtimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), "");
    }
}
