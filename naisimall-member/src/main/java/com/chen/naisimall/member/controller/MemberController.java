package com.chen.naisimall.member.controller;

import java.util.Arrays;
import java.util.Map;

import com.chen.common.exception.BizCodeEnume;
import com.chen.naisimall.member.exception.PhoneExistException;
import com.chen.naisimall.member.exception.UsernameExistException;
import com.chen.naisimall.member.feign.CouponFeignService;
import com.chen.naisimall.member.vo.MemberLoginVo;
import com.chen.naisimall.member.vo.MemberRegistVo;
import com.chen.naisimall.member.vo.SocialUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.bind.annotation.*;

import com.chen.naisimall.member.entity.MemberEntity;
import com.chen.naisimall.member.service.MemberService;
import com.chen.common.utils.PageUtils;
import com.chen.common.utils.R;


/**
 * 会员
 *
 * @author csjstart
 * @email 1047715631.com
 * @date 2021-05-27 22:30:20
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    CouponFeignService couponFeignService;

    /**
     * @return 测试远程调用
     */
    @RequestMapping("/coupons")
    public R test() {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setNickname("张三");
        R membercoupon = couponFeignService.membercoupon();
        return R.ok().put("member", memberEntity).put("coupons", membercoupon.get("coupons"));
    }

    @PostMapping("/oauth2/login")
    public R oauthlogin(@RequestBody SocialUser socialUser) throws Exception {
        MemberEntity entity = memberService.login(socialUser);
        if (entity != null){
            //TODO1,登陆成功处理
            return R.ok().setData(entity);
        }else {
            return R.error(BizCodeEnume.LOGINACCT_PASSWORD_INVAILD_EXCEPTION.getCode(),BizCodeEnume.LOGINACCT_PASSWORD_INVAILD_EXCEPTION.getMsg());
        }
    }

    @PostMapping("/login")
    public R login(@RequestBody MemberLoginVo vo){
        MemberEntity entity = memberService.login(vo);
        if (entity != null){
            return R.ok().setData(entity);
        }else {
            return R.error(BizCodeEnume.LOGINACCT_PASSWORD_INVAILD_EXCEPTION.getCode(),BizCodeEnume.LOGINACCT_PASSWORD_INVAILD_EXCEPTION.getMsg());
        }
    }

    @PostMapping("/regist")
    public R regist(@RequestBody MemberRegistVo vo){
        try {
            memberService.regist(vo);
        } catch (PhoneExistException e) {
            return R.error(BizCodeEnume.PHONE_EXIST_EXCEPTION.getCode(), BizCodeEnume.PHONE_EXIST_EXCEPTION.getMsg());
        }catch (UsernameExistException e){
            return R.error(BizCodeEnume.USER_EXIST_EXCEPTION.getCode(), BizCodeEnume.USER_EXIST_EXCEPTION.getMsg());
        }
        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MemberEntity member) {
        memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MemberEntity member) {
        memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("member:member:delete")
    public R delete(@RequestBody Long[] ids) {
        memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
