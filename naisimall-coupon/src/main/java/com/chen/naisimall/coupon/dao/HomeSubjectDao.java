package com.chen.naisimall.coupon.dao;

import com.chen.naisimall.coupon.entity.HomeSubjectEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 首页专题表【jd首页下面很多专题，每个专题链接新的页面，展示专题商品信息】
 *
 * @author csjstart
 * @email 1047715631.com
 * @date 2021-05-27 22:07:54
 */
@Mapper
public interface HomeSubjectDao extends BaseMapper<HomeSubjectEntity> {

}
