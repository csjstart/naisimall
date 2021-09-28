package com.chen.naisimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.common.utils.PageUtils;
import com.chen.naisimall.ware.vo.MergeVo;
import com.chen.naisimall.ware.entity.PurchaseEntity;
import com.chen.naisimall.ware.vo.PurchaseDoneVo;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author csjstart
 * @email 1047715631.com
 * @date 2021-05-27 22:57:25
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询未领取的采购单
     *
     * @param params
     * @return
     */
    PageUtils queryPageUnreceivePurchase(Map<String, Object> params);

    /**
     * 合并采购单
     *
     * @param mergeVo
     */
    void mergePurchase(MergeVo mergeVo);

    /**
     * 领取采购单
     *
     * @param ids
     */
    void received(List<Long> ids);

    /**
     * 完成采购
     *
     * @param doneVo
     */
    void done(PurchaseDoneVo doneVo);
}

