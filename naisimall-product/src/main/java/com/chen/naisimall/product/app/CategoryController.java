package com.chen.naisimall.product.app;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chen.naisimall.product.entity.CategoryEntity;
import com.chen.naisimall.product.service.CategoryService;
import com.chen.common.utils.R;


/**
 * 商品三级分类
 *
 * @author csjstart
 * @email 1047715631.com
 * @date 2021-05-27 22:02:30
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 列表
     */
    @RequestMapping("/list/tree")
    public R list() {
        List<CategoryEntity> entities = categoryService.listWithTree();

        return R.ok().put("data", entities);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    public R info(@PathVariable("catId") Long catId) {
        CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("data", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CategoryEntity category) {
        categoryService.save(category);

        return R.ok();
    }

    @RequestMapping("/update/sort")
    public R updateSort(@RequestBody CategoryEntity[] category) {
        categoryService.updateBatchById(Arrays.asList(category));

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:category:update")
    public R update(@RequestBody CategoryEntity category) {
        categoryService.updateCascade(category);

        return R.ok();
    }

    /**
     * 删除
     *
     * @RequestBody:获取请求体,必须发POST请求 SpringMVC自动将请求体中的数据(json), 转为对应的对象
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] catIds) {
        //categoryService.removeByIds(Arrays.asList(catIds));
        categoryService.removeMenuByIds(Arrays.asList(catIds));
        return R.ok();
    }

}
