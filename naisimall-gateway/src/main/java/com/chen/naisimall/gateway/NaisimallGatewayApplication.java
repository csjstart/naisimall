package com.chen.naisimall.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author woita
 */
@EnableDiscoveryClient

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class NaisimallGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(NaisimallGatewayApplication.class, args);
    }

}
