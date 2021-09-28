package com.chen.naisimall.order.dao;

import com.chen.naisimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 *
 * @author csjstart
 * @email 1047715631.com
 * @date 2021-05-27 22:45:55
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {

}
