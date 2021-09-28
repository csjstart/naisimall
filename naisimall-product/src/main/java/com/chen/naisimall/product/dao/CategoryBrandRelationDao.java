package com.chen.naisimall.product.dao;

import com.chen.naisimall.product.entity.CategoryBrandRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 品牌分类关联
 *
 * @author csjstart
 * @email 1047715631.com
 * @date 2021-05-27 22:02:30
 */
@Mapper
public interface CategoryBrandRelationDao extends BaseMapper<CategoryBrandRelationEntity> {

    /**
     * 修改categoryBrandRelation关联表
     *
     * @param catId
     * @param name
     */
    void updateCategory(@Param("catId") Long catId, @Param("name") String name);
}
