package com.chen.naisimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.common.utils.PageUtils;
import com.chen.naisimall.product.entity.CategoryEntity;
import com.chen.naisimall.product.vo.Catelog2Vo;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author csjstart
 * @email 1047715631.com
 * @date 2021-05-27 22:02:30
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> listWithTree();

    void removeMenuByIds(List<Long> asList);

    /**
     * 找到catelog的完整路径
     * [父/子/孙]
     *
     * @param catelogId
     * @return
     */
    Long[] findCatelogPath(Long catelogId);

    void updateCascade(CategoryEntity category);

    /**
     * 查出所有一级分类
     * @return
     */
    List<CategoryEntity> getLevel1Categorys();

    /**
     * 查出所有的分类
     * @return
     */
    Map<String, List<Catelog2Vo>> getCatelogJson();

}

