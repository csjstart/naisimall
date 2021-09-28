package com.chen.naisimall.order.service.impl;

import com.chen.naisimall.order.entity.OrderEntity;
import com.chen.naisimall.order.entity.OrderReturnReasonEntity;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.common.utils.PageUtils;
import com.chen.common.utils.Query;

import com.chen.naisimall.order.dao.OrderItemDao;
import com.chen.naisimall.order.entity.OrderItemEntity;
import com.chen.naisimall.order.service.OrderItemService;


@Service("orderItemService")
@RabbitListener(queues = {"hello.java.Queue"})
public class OrderItemServiceImpl extends ServiceImpl<OrderItemDao, OrderItemEntity> implements OrderItemService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderItemEntity> page = this.page(
                new Query<OrderItemEntity>().getPage(params),
                new QueryWrapper<OrderItemEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * queues:声明需要监听的所有队列
     * @param message
     * 类型:class org.springframework.amqp.core.Message
     *
     * 参数可以写一下类型
     * 1,Message message:原生消息详细信息,头+体
     * 2,T<发送的消息类型>  OrderReturnReasonEntity content
     * 3,Channel channel:当前传输数据的通道
     *
     * Queue:可以很多人都来监听.只要收到消息,队列删除消息,而且只能有一个收到此消息
     * 场景:
     *      1),订单服务启动多个:同一个消息,只能有一个客户端收到
     *      2),只有一个消息完全处理完,方法运行结束,我们就可以接受到下一个消息
     *      TODO 默认自动ACK
     */

    //@RabbitListener(queues = {"hello.java.Queue"})
    @RabbitHandler
    public void receiveMessage(Message message, OrderReturnReasonEntity content,
                               Channel channel) throws InterruptedException {
        //Body:'{"id":1,"name":"奶思","sort":null,"status":null,"createTime":1631451804405}'
        System.out.println("接收到消息..." + content);
        byte[] body = message.getBody();
        //消息头属性信息
        MessageProperties messageProperties = message.getMessageProperties();
        //Thread.sleep(3000);
        System.out.println("消息处理完成==>" + content.getName());
        //channel内按顺序自增的
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        System.out.println("deliveryTag===>"+ deliveryTag);
        //签收货物,非批量模式
        try {

            if (deliveryTag % 2 == 0) {
                channel.basicAck(deliveryTag,false);
                System.out.println("签收了货物..." + deliveryTag);
            }else {
                //退货 requeue=false 丢弃 requeue=true 发回服务器,服务器重新入队
                //(long deliveryTag, boolean multiple, boolean requeue)
                channel.basicNack(deliveryTag,false,false);
                //(long deliveryTag, boolean requeue)
                //channel.basicReject();
                System.out.println("没有签收货物..." + deliveryTag);
            }
        } catch (IOException e) {
            //网络中断
            e.printStackTrace();
        }
    }

    @RabbitHandler
    public void receiveMessage2(Message message,OrderEntity content,Channel channel) throws InterruptedException {
        //Body:'{"id":1,"name":"奶思","sort":null,"status":null,"createTime":1631451804405}'
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            channel.basicAck(deliveryTag,false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("消息处理完成==>" + content);
    }

}