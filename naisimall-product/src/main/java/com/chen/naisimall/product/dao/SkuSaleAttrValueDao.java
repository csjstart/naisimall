package com.chen.naisimall.product.dao;

import com.chen.naisimall.product.entity.SkuSaleAttrValueEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chen.naisimall.product.vo.SkuItemSaleAttrVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * sku销售属性&值
 *
 * @author csjstart
 * @email 1047715631.com
 * @date 2021-05-27 22:02:30
 */
@Mapper
public interface SkuSaleAttrValueDao extends BaseMapper<SkuSaleAttrValueEntity> {

    List<SkuItemSaleAttrVo> getSaleAttrsBySpuId(@Param("spuId") Long spuId);

    List<String> getSkuSaleAttrValuesAsStringList(@Param("skuId") Long skuId);
}
