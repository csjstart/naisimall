package com.chen.naisimall.order;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 使用RabbitMQ
 *      1，引入amqp场景：RabbitAutoConfiguration就会就会生效
 *      2，给容器中自动配置了
 *          RabbitTemplate,AmqpAdmin,CachingConnectionFactory,RabbitMessagingTemplate
 *          所有的属性都是
 *          @ConfigurationProperties(prefix = "spring.rabbitmq")
 *          public class RabbitProperties {
 *      3,给配置文件中配置spring.rabbitmq信息
 *      4,@EnableRabbit:@EnableXxxxx
 *      5,监听消息:使用@RabbitListener:必须有@EnableRabbit
 *          @RabbitListener:类+方法(监听那些队列)
 *          @RabbitHandler:标在方法上(重载区分不同的消息)
 *
 * @author woita
 */
@EnableRabbit
@EnableRedisHttpSession
@EnableDiscoveryClient
@SpringBootApplication
public class NaisimallOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(NaisimallOrderApplication.class, args);
    }

}
