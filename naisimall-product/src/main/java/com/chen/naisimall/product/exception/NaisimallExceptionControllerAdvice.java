package com.chen.naisimall.product.exception;

import com.chen.common.exception.BizCodeEnume;
import com.chen.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

/**
 * @author chenouba
 * @create 2021-06-21 23:38
 * 集中处理所有异常
 * @ControllerAdvice(basePackages = "com.chen.naisimall.product.controller")
 * @ResponseBody 以上两个注解使用@RestControllerAdvice(basePackages = "com.chen.naisimall.product.controller")代替
 */
@Slf4j

@RestControllerAdvice(basePackages = "com.chen.naisimall.product.app")
public class NaisimallExceptionControllerAdvice {

    @ExceptionHandler(value = Exception.class)

    public R handleVaildException(MethodArgumentNotValidException e) {
        log.error("数据校验出现问题{},异常类型:{}", e.getMessage(), e.getClass());
        BindingResult bindingResult = e.getBindingResult();

        HashMap<String, String> errorMap = new HashMap<>(16);

        bindingResult.getFieldErrors().forEach((fieldError -> {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        }));
        return R.error(BizCodeEnume.VAILD_EXCEPTION.getCode(), BizCodeEnume.VAILD_EXCEPTION.getMsg()).put("data", errorMap);
    }

    @ExceptionHandler(value = Throwable.class)
    public R handlerException(Throwable throwable) {
        log.error("错误", throwable);
        return R.error(BizCodeEnume.UNKNOW_EXCEPTION.getCode(), BizCodeEnume.UNKNOW_EXCEPTION.getMsg());
    }
}
