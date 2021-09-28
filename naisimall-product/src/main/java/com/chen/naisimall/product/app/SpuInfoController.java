package com.chen.naisimall.product.app;

import java.util.Arrays;
import java.util.Map;

import com.chen.naisimall.product.vo.SpuSaveVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.chen.naisimall.product.entity.SpuInfoEntity;
import com.chen.naisimall.product.service.SpuInfoService;
import com.chen.common.utils.PageUtils;
import com.chen.common.utils.R;


/**
 * spu信息
 *
 * @author csjstart
 * @email 1047715631.com
 * @date 2021-05-27 22:02:30
 */
@RestController
@RequestMapping("product/spuinfo")
public class SpuInfoController {
    @Autowired
    private SpuInfoService spuInfoService;

    /**
     * /product/spuinfo/{spuId}/up
     */
    @PostMapping("/{spuId}/up")
    public R spuUp(@PathVariable("spuId")Long spuId) {
        spuInfoService.up(spuId);

        return R.ok();
    }

    /**
     * 列表
     *
     * @RequiresPermissions("product:spuinfo:list")
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = spuInfoService.queryPageByCondition(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     *
     * @RequiresPermissions("product:spuinfo:info")
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        SpuInfoEntity spuInfo = spuInfoService.getById(id);

        return R.ok().put("spuInfo", spuInfo);
    }

    /**
     * 保存
     *
     * @RequiresPermissions("product:spuinfo:save") spuInfoService.save(spuInfo);
     */
    @RequestMapping("/save")
    public R save(@RequestBody SpuSaveVo vo) {


        spuInfoService.saveSpuInfo(vo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:spuinfo:update")
    public R update(@RequestBody SpuInfoEntity spuInfo) {
        spuInfoService.updateById(spuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:spuinfo:delete")
    public R delete(@RequestBody Long[] ids) {
        spuInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
