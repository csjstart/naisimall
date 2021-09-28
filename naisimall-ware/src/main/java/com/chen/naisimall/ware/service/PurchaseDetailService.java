package com.chen.naisimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.common.utils.PageUtils;
import com.chen.naisimall.ware.entity.PurchaseDetailEntity;

import java.util.List;
import java.util.Map;

/**
 * @author csjstart
 * @email 1047715631.com
 * @date 2021-05-27 22:57:25
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 通过采购单查询所有采购项
     *
     * @param id
     * @return
     */
    List<PurchaseDetailEntity> listDetailByPurchaseId(Long id);
}

