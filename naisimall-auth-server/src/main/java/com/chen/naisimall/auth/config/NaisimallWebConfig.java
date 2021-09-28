package com.chen.naisimall.auth.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 1,写一个配置类，实现WebMvcConfigurer
 * 2,重写addViewControllers(ViewControllerRegistry registry)方法,
 * 3,给视图控制器（ViewControllerRegistry）添加注册视图
 * 视图映射
 */
@Configuration
public class NaisimallWebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        /**
         * @GetMapping("/login.html")
         *     public String loginPage(){
         *         return "login";
         */
        //registry.addViewController("/login.html").setViewName("login");
        registry.addViewController("/reg.html").setViewName("reg");
    }
}
