package com.chen.naisimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.common.utils.PageUtils;
import com.chen.naisimall.ware.entity.UndoLogEntity;

import java.util.Map;

/**
 * @author csjstart
 * @email 1047715631.com
 * @date 2021-05-27 22:57:25
 */
public interface UndoLogService extends IService<UndoLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

