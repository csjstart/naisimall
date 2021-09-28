package com.chen.naisimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.common.utils.PageUtils;
import com.chen.naisimall.member.entity.MemberEntity;
import com.chen.naisimall.member.exception.PhoneExistException;
import com.chen.naisimall.member.exception.UsernameExistException;
import com.chen.naisimall.member.vo.MemberLoginVo;
import com.chen.naisimall.member.vo.MemberRegistVo;
import com.chen.naisimall.member.vo.SocialUser;

import java.util.Map;

/**
 * 会员
 *
 * @author csjstart
 * @email 1047715631.com
 * @date 2021-05-27 22:30:20
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 注册
     * @param vo
     */
    void regist(MemberRegistVo vo);

    void checkPhoneUnique(String phone) throws PhoneExistException;

    void checkUserNameUnique(String userName) throws UsernameExistException;

    MemberEntity login(MemberLoginVo vo);

    MemberEntity login(SocialUser socialUser) throws Exception;
}

