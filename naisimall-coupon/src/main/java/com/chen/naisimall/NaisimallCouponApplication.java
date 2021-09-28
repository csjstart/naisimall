package com.chen.naisimall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author woita
 */
@EnableDiscoveryClient
@SpringBootApplication
public class NaisimallCouponApplication {

    public static void main(String[] args) {
        SpringApplication.run(NaisimallCouponApplication.class, args);
    }
}
