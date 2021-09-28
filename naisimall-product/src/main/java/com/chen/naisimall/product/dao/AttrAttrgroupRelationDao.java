package com.chen.naisimall.product.dao;

import com.chen.naisimall.product.entity.AttrAttrgroupRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性&属性分组关联
 *
 * @author csjstart
 * @email 1047715631.com
 * @date 2021-05-27 22:02:30
 */
@Mapper
public interface AttrAttrgroupRelationDao extends BaseMapper<AttrAttrgroupRelationEntity> {

    /**
     * 批量删除
     *
     * @param entities
     */
    void deleteBatchRelation(@Param("entities") List<AttrAttrgroupRelationEntity> entities);

}
