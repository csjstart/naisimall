package com.chen.naisimall.auth.feign;

import com.chen.common.utils.R;
import com.chen.naisimall.auth.vo.SocialUser;
import com.chen.naisimall.auth.vo.UserLoginVo;
import com.chen.naisimall.auth.vo.UserRegistVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("naisimall-member")
public interface MemberFeignService {

    @PostMapping("/member/member/regist")
    R regist(@RequestBody UserRegistVo vo);

    @PostMapping("/member/member/login")
    R login(@RequestBody UserLoginVo vo);

    @PostMapping("/member/member/oauth2/login")
    public R oauthlogin(@RequestBody SocialUser socialUser) throws Exception;

}
