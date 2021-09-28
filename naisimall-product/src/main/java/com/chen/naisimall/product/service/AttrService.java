package com.chen.naisimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.common.utils.PageUtils;
import com.chen.naisimall.product.entity.AttrEntity;
import com.chen.naisimall.product.vo.AttrGroupRelationVo;
import com.chen.naisimall.product.vo.AttrRespVo;
import com.chen.naisimall.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author csjstart
 * @email 1047715631.com
 * @date 2021-05-27 22:02:30
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attr);

    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type);

    AttrRespVo getAttrInfo(Long attrId);

    void updateAttr(AttrRespVo attr);

    /**
     * 根据分组ID找到属性
     *
     * @param attrgroupId
     * @return
     */
    List<AttrEntity> getRelationAttr(Long attrgroupId);

    void deleteRelation(AttrGroupRelationVo[] vos);

    PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId);

    /**
     * 在指定的所有属性集合里面,挑出检索属性
     * @param attrIds
     * @return
     */
    List<Long> selectSearchAttrIds(List<Long> attrIds);
}

