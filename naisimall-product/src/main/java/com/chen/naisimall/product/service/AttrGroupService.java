package com.chen.naisimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.common.utils.PageUtils;
import com.chen.naisimall.product.entity.AttrGroupEntity;
import com.chen.naisimall.product.vo.AttrGroupWithAttrsVo;
import com.chen.naisimall.product.vo.SkuItemVo;
import com.chen.naisimall.product.vo.SpuItemAttrGroupVo;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author csjstart
 * @email 1047715631.com
 * @date 2021-05-27 22:02:30
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, Long catelogId);

    List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId);

    List<SpuItemAttrGroupVo> getAttrGroupWithAttrsBySpuId(Long spuId, Long catelogId);
}

