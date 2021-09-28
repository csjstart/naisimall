package com.chen.naisimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.common.utils.PageUtils;
import com.chen.naisimall.ware.entity.WareInfoEntity;

import java.util.Map;

/**
 * 仓库信息
 *
 * @author csjstart
 * @email 1047715631.com
 * @date 2021-05-27 22:57:25
 */
public interface WareInfoService extends IService<WareInfoEntity> {

    /**
     * 查询仓库列表
     *
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);
}

