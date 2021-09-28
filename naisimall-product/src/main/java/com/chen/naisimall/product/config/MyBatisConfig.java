package com.chen.naisimall.product.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author chenouba
 * Mybatis分页插件
 * @create 2021-06-27 14:23
 * @EnableTransactionManagement 开启使用事务
 */
@Configuration
@EnableTransactionManagement
@MapperScan("com.chen.naisimall.product.dao")
public class MyBatisConfig {

    /**
     * 引入分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        //设置请求的页面大于最后页后操作,true调回到首页,false继续请求,默认为false
        paginationInterceptor.setOverflow(true);
        //设置最大单页限制数量,默认500条,-1不限制
        paginationInterceptor.setLimit(1000);

        return paginationInterceptor;
    }
}
