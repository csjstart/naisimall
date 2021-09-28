package com.chen.naisimall.ware.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author woita
 */
@EnableTransactionManagement
@MapperScan("com.chen.naisimall.ware.dao")
@Configuration
public class WareMybatisConfig {

    /**
     * 引入分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        //设置请求的页面大于最后页后操作,true调回到首页,false继续请求,默认为false
        //paginationInterceptor.setOverflow(true);
        //设置最大单页限制数量,默认500条,-1不限制,
        //paginationInterceptor.setLimit(1000);

        return paginationInterceptor;
    }

}
