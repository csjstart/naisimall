package com.chen.naisimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.common.utils.PageUtils;
import com.chen.naisimall.coupon.entity.HomeSubjectEntity;

import java.util.Map;

/**
 * 首页专题表【jd首页下面很多专题，每个专题链接新的页面，展示专题商品信息】
 *
 * @author csjstart
 * @email 1047715631.com
 * @date 2021-05-27 22:07:54
 */
public interface HomeSubjectService extends IService<HomeSubjectEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

