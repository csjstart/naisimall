package com.chen.naisimall.order.dao;

import com.chen.naisimall.order.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 *
 * @author csjstart
 * @email 1047715631.com
 * @date 2021-05-27 22:45:55
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {

}
