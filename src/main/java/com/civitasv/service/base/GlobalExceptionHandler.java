package com.civitasv.service.base;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import com.civitasv.handler.CustomException;
import com.civitasv.handler.Result;
import com.civitasv.handler.ResultCode;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 全局异常
     *
     * @param e 异常
     * @return 异常信息
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<String> error(Exception e) {
        e.printStackTrace();
        return new Result<String>().code(ResultCode.NOT_FOUND).message(e.getClass().toString()).success(false).data(e.getMessage());
    }

    /**
     * 自定义异常
     *
     * @param e 异常
     * @return 异常信息
     */
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public Result<String> error(CustomException e) {
        e.printStackTrace();
        return new Result<String>().code(e.getCode()).message(e.getMsg()).success(false).data(e.getMessage());
    }
}
