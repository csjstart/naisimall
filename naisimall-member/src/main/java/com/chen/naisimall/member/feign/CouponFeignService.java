package com.chen.naisimall.member.feign;

import com.chen.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author chenouba
 * @create 2021-05-28 0:39
 * 远程调用的接口,路径为完整的请求
 */
@FeignClient("naisimall-coupon")
public interface CouponFeignService {

    @RequestMapping("/coupon/coupon/member/list")
    public R membercoupon();
}
