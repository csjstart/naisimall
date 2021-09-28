package com.chen.naisimall.thirdparty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author woita
 */
@SpringBootApplication
@EnableDiscoveryClient
public class NaisimallThirdPartyApplication {

    public static void main(String[] args) {
        SpringApplication.run(NaisimallThirdPartyApplication.class, args);
    }

}
