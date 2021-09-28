package com.chen.naisimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.common.utils.PageUtils;
import com.chen.naisimall.product.entity.SpuImagesEntity;

import java.util.List;
import java.util.Map;

/**
 * spu图片
 *
 * @author csjstart
 * @email 1047715631.com
 * @date 2021-05-27 22:02:30
 */
public interface SpuImagesService extends IService<SpuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存Spu的图片集
     *
     * @param id
     * @param images
     */
    void saveImages(Long id, List<String> images);
}

