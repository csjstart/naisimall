package com.chen.naisimall.ware.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.chen.naisimall.ware.vo.MergeVo;
import com.chen.naisimall.ware.vo.PurchaseDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.chen.naisimall.ware.entity.PurchaseEntity;
import com.chen.naisimall.ware.service.PurchaseService;
import com.chen.common.utils.PageUtils;
import com.chen.common.utils.R;


/**
 * 采购信息
 *
 * @author csjstart
 * @email 1047715631.com
 * @date 2021-05-27 22:57:25
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    /**
     * /ware/purchase/done
     *
     * @return
     */
    @PostMapping("/done")
    public R finish(@RequestBody PurchaseDoneVo doneVo) {

        purchaseService.done(doneVo);
        return R.ok();
    }


    /**
     * /ware/purchase/received
     *
     * @return
     */
    @PostMapping("/received")
    public R received(@RequestBody List<Long> ids) {

        purchaseService.received(ids);
        return R.ok();
    }

    /**
     * ware/purchase/merge
     *
     * @param mergeVo
     * @return
     */
    @PostMapping("/merge")
    public R merge(@RequestBody MergeVo mergeVo) {
        purchaseService.mergePurchase(mergeVo);
        return R.ok();
    }
    //ware/purchase/unreceive/list

    /**
     * 列表
     */
    @RequestMapping("/unreceive/list")
    public R unreceivelist(@RequestParam Map<String, Object> params) {
        PageUtils page = purchaseService.queryPageUnreceivePurchase(params);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("ware:purchase:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:purchase:info")
    public R info(@PathVariable("id") Long id) {
        PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:purchase:save")
    public R save(@RequestBody PurchaseEntity purchase) {
        purchase.setUpdateTime(new Date());
        purchase.setCreateTime(new Date());
        purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:purchase:update")
    public R update(@RequestBody PurchaseEntity purchase) {
        purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:purchase:delete")
    public R delete(@RequestBody Long[] ids) {
        purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
