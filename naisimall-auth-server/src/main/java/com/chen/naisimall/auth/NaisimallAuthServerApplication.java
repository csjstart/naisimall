package com.chen.naisimall.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * SpringSession核心原理
 *  1),@EnableRedisHttpSession导入@Import({RedisHttpSessionConfiguration.class})配置
 *      1,给容器中添加了一个组件
 *          SessionRepository===>RedisIndexedSessionRepository  :redis操作session,session的增删改查封装类
 *      2,SessionRepositoryFilter===>Filter :Session存储过滤器:每个请求过来都必须经过Filter
 *          1,创建的时候,就自动从容器中获取到了SessionRepository
 *          2,原始的request,response都被包装了,SessionRepositoryRequestWrapper,SessionRepositoryRequestWrapper==>SessionRepositoryFilter
 *          3,以后获取Session,request.getSession()
 *          4,wrapperRequest.getSession()====>SessionRepository中获取到的,--->Redis
 *      装饰者模式
 *      自动延期:Redis中的数据也是有过期时间的
 *
 *
 */
@EnableRedisHttpSession //整合redis作为session的存储
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class NaisimallAuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NaisimallAuthServerApplication.class, args);
    }

}
