package com.chen.naisimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.common.utils.PageUtils;
import com.chen.naisimall.product.entity.SpuInfoDescEntity;
import com.chen.naisimall.product.entity.SpuInfoEntity;
import com.chen.naisimall.product.vo.SpuSaveVo;

import java.util.Map;

/**
 * spu信息
 *
 * @author csjstart
 * @email 1047715631.com
 * @date 2021-05-27 22:02:30
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存spuInfo信息
     *
     * @param vo
     */
    void saveSpuInfo(SpuSaveVo vo);

    /**
     * 保存spu基本信息
     *
     * @param infoEntity
     */
    void saveBaseSpuInfo(SpuInfoEntity infoEntity);


    /**
     * 条件查询商品信息
     *
     * @param params
     * @return
     */
    PageUtils queryPageByCondition(Map<String, Object> params);

    /**
     * 商品上架
     * @param spuId
     */
    void up(Long spuId);
}

