package com.chen.naisimall.product.dao;

import com.chen.naisimall.product.entity.AttrEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品属性
 *
 * @author csjstart
 * @email 1047715631.com
 * @date 2021-05-27 22:02:30
 */
@Mapper
public interface AttrDao extends BaseMapper<AttrEntity> {

    /**
     * select attr_id FROM pms_attr WHERE attr_id in(?) and search_type = 1
     * @param attrIds
     * @return
     */
    List<Long> selectSearchAttrIds(@Param("attrIds") List<Long> attrIds);
}
