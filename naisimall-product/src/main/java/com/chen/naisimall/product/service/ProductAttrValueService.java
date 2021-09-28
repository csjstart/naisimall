package com.chen.naisimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.common.utils.PageUtils;
import com.chen.naisimall.product.entity.ProductAttrValueEntity;

import java.util.List;
import java.util.Map;

/**
 * spu属性值
 *
 * @author csjstart
 * @email 1047715631.com
 * @date 2021-05-27 22:02:30
 */
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存Spu的规格参数
     *
     * @param collect
     */
    void saveProductAttr(List<ProductAttrValueEntity> collect);

    /**
     * 获取spu规格
     *
     * @param spuId
     * @return
     */
    List<ProductAttrValueEntity> baseAttrListforspu(Long spuId);

    /**
     * 更新spu信息
     *
     * @param spuId
     * @param entities
     */
    void updateSpuAttr(Long spuId, List<ProductAttrValueEntity> entities);
}

