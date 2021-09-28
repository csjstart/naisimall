package com.chen.naisimall.product.dao;

import com.chen.naisimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 *
 * @author csjstart
 * @email 1047715631.com
 * @date 2021-05-27 22:02:30
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {

}
