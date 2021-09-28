package com.chen.naisimall.product.dao;

import com.chen.naisimall.product.entity.AttrGroupEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chen.naisimall.product.vo.SkuItemVo;
import com.chen.naisimall.product.vo.SpuItemAttrGroupVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性分组
 *
 * @author csjstart
 * @email 1047715631.com
 * @date 2021-05-27 22:02:30
 */
@Mapper
public interface AttrGroupDao extends BaseMapper<AttrGroupEntity> {

    List<SpuItemAttrGroupVo> getAttrGroupWithAttrsBySpuId(@Param("spuId") Long spuId, @Param("catelogId") Long catelogId);

}
