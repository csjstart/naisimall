package com.chen.naisimall.product.feign;

import com.chen.common.to.SkuReductionTo;
import com.chen.common.to.SpuBoundTo;
import com.chen.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author chenouba
 * @create 2021-07-24 19:36
 */
@FeignClient("naisimall-coupon")
public interface CouponFeignService {
    /**
     * 1,CouponFeignService.saveSpuBounds(spuBoundTo);
     * 1),@RequestBody将这个对象转为json
     * 2),找到naisimall-coupon服务,给/coupon/spubounds/save发送请求
     * 将上一步转的json放在请求体位置,发送请求
     * 3),对方服务收到请求,请求体里有json数据
     * (@RequestBody SpuBoundsEntity spuBounds),将请求体的json转为SpuBoundsEntity
     * 只要json数据模型是兼容的,双方服务无需使用同一个to
     * 保存Spu的积分信息
     *
     * @param spuBoundTo
     */
    @PostMapping("/coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundTo spuBoundTo);

    /**
     * 4),sku的优惠,满减等信息:naisimall_sms->sms_sku_ladder sms_sku_full_reduction sms_member_price
     *
     * @param skuReductionTo
     * @return
     */
    @PostMapping("/coupon/skufullreduction/saveInfo")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);
}
