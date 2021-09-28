package com.chen.naisimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.common.utils.PageUtils;
import com.chen.naisimall.member.entity.MemberLoginLogEntity;

import java.util.Map;

/**
 * 会员登录记录
 *
 * @author csjstart
 * @email 1047715631.com
 * @date 2021-05-27 22:30:20
 */
public interface MemberLoginLogService extends IService<MemberLoginLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

