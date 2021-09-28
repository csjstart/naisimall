package com.chen.naisimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.common.utils.PageUtils;
import com.chen.naisimall.product.entity.SkuInfoEntity;
import com.chen.naisimall.product.vo.SkuItemVo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * sku信息
 *
 * @author csjstart
 * @email 1047715631.com
 * @date 2021-05-27 22:02:30
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存sku基本信息
     *
     * @param skuInfoEntity
     */
    void saveSkuInfo(SkuInfoEntity skuInfoEntity);

    /**
     * 条件查询商品信息
     *
     * @param params
     * @return
     */
    PageUtils queryPageByCondition(Map<String, Object> params);

    /**
     * 查出spuId对应所有的sku
     * @param spuId
     * @return
     */
    List<SkuInfoEntity> getSkusBySpuId(Long spuId);

    /**
     * 商品详情
     * @param skuId
     * @return
     */
    SkuItemVo item(Long skuId) throws ExecutionException, InterruptedException;
}

