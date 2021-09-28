package com.chen.naisimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.common.utils.PageUtils;
import com.chen.naisimall.order.entity.OrderEntity;

import java.util.Map;

/**
 * 订单
 *
 * @author csjstart
 * @email 1047715631.com
 * @date 2021-05-27 22:45:55
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

