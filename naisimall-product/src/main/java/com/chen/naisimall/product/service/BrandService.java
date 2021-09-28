package com.chen.naisimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.common.utils.PageUtils;
import com.chen.naisimall.product.entity.BrandEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌
 *
 * @author csjstart
 * @email 1047715631.com
 * @date 2021-05-27 22:02:30
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void updateDetail(BrandEntity brand);

    List<BrandEntity> getBrandsByIds(List<Long> brandIds);
}

