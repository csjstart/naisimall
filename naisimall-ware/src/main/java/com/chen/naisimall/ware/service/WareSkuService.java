package com.chen.naisimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.common.utils.PageUtils;
import com.chen.naisimall.ware.entity.WareSkuEntity;
import com.chen.naisimall.ware.vo.SkuHasStockVo;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author csjstart
 * @email 1047715631.com
 * @date 2021-05-27 22:57:25
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 完成采购单,修改库存
     *
     * @param skuId
     * @param wareId
     * @param skuNum
     */
    void addStock(Long skuId, Long wareId, Integer skuNum);

    /**
     * 查询是否有库存
     * @param skuIds
     * @return
     */
    List<SkuHasStockVo> getSkusHasStock(List<Long> skuIds);
}

