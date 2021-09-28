package com.chen.naisimall.ware;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author woita
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class NaisimallWareApplication {

    public static void main(String[] args) {
        SpringApplication.run(NaisimallWareApplication.class, args);
    }

}
