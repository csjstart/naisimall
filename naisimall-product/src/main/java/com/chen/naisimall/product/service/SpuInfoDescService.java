package com.chen.naisimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.common.utils.PageUtils;
import com.chen.naisimall.product.entity.SpuInfoDescEntity;

import java.util.Map;

/**
 * spu信息介绍
 *
 * @author csjstart
 * @email 1047715631.com
 * @date 2021-05-27 22:02:30
 */
public interface SpuInfoDescService extends IService<SpuInfoDescEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存spu描述信息
     *
     * @param descEntity
     */
    void saveSpuInfoDesc(SpuInfoDescEntity descEntity);
}

