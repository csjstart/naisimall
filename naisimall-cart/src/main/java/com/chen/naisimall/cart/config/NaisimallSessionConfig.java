package com.chen.naisimall.cart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * @author woita
 * 配置Session作用域等
 */
@EnableRedisHttpSession
@Configuration
public class NaisimallSessionConfig {

    @Bean
    public CookieSerializer cookieSerializer(){
        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
        cookieSerializer.setDomainName("naisimall.com");
        cookieSerializer.setCookieName("NAISISESSION");
        return cookieSerializer;
    }
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer(){
        return new GenericJackson2JsonRedisSerializer();
    }

}
