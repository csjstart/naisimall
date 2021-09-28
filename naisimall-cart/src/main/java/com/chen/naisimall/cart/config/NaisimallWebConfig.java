package com.chen.naisimall.cart.config;

import com.chen.naisimall.cart.interceptor.CartInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * SpringBoot给Web添加拦截器,
 * Web配置
 * @author woita
 */
@Configuration
public class NaisimallWebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册列表
        InterceptorRegistration addInterceptor = registry.addInterceptor(new CartInterceptor());
        addInterceptor.addPathPatterns("/**");
    }
}
