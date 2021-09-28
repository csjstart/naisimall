package com.chen.naisimall.ware.dao;

import com.chen.naisimall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商品库存
 *
 * @author csjstart
 * @email 1047715631.com
 * @date 2021-05-27 22:57:25
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    /**
     * 修改库存
     *
     * @param skuId
     * @param wareId
     * @param skuNum
     */
    void addStock(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("skuNum") Integer skuNum);

    /**
     * //查询当前sku的库存量
     * @param skuId
     * @return
     */
    Long getSkuStock(Long skuId);
}
