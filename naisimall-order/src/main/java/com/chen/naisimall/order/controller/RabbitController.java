package com.chen.naisimall.order.controller;

import com.chen.naisimall.order.entity.OrderEntity;
import com.chen.naisimall.order.entity.OrderReturnReasonEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;
@Slf4j
@RestController
public class RabbitController {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMq")
    public String sendMq(@RequestParam(value = "num",defaultValue = "10")Integer num) {

        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0){
                OrderReturnReasonEntity reasonEntity = new OrderReturnReasonEntity();
                reasonEntity.setId(1l);
                reasonEntity.setCreateTime(new Date());
                reasonEntity.setName("奶思" + i);
                rabbitTemplate.convertAndSend("hello.java.exchange","hello.java",reasonEntity,new CorrelationData(UUID.randomUUID().toString()));
            }else {
                OrderEntity orderEntity = new OrderEntity();
                orderEntity.setOrderSn(UUID.randomUUID().toString());
                rabbitTemplate.convertAndSend("hello.java.exchange","hello222.java",orderEntity,new CorrelationData(UUID.randomUUID().toString()));
            }

            log.info("消息发送完成{}");
        }

        return "ok";

    }
}
