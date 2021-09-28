package com.chen.naisimall.coupon.dao;

import com.chen.naisimall.coupon.entity.CouponHistoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券领取历史记录
 *
 * @author csjstart
 * @email 1047715631.com
 * @date 2021-05-27 22:07:53
 */
@Mapper
public interface CouponHistoryDao extends BaseMapper<CouponHistoryEntity> {

}
