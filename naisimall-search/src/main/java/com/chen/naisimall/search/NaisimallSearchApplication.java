package com.chen.naisimall.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author woita
 */
@EnableRedisHttpSession
@EnableFeignClients//开启远程调用
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
public class NaisimallSearchApplication {

    public static void main(String[] args) {
        // 解决elasticsearch启动保存问题
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(NaisimallSearchApplication.class, args);
    }

}
