package com.chen.naisimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.common.utils.PageUtils;
import com.chen.naisimall.coupon.entity.SeckillSessionEntity;

import java.util.Map;

/**
 * 秒杀活动场次
 *
 * @author csjstart
 * @email 1047715631.com
 * @date 2021-05-27 22:07:53
 */
public interface SeckillSessionService extends IService<SeckillSessionEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

