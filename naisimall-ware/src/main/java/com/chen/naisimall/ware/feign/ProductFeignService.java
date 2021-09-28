package com.chen.naisimall.ware.feign;

import com.chen.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author woita
 */
@FeignClient("naisimall-product")
public interface ProductFeignService {


    /**
     * 1),让所有请求过网关
     * 1,@FeignClient("naisimall-gateway"):给naisimall-gateway所在的机器发请求
     * 2,/api/product/skuinfo/info/{skuId}
     * 2),直接让后台指定服务处理
     *
     * @return
     * @FeignClient("naisimall-product") 2,/product/skuinfo/info/{skuId}
     */
    @RequestMapping("/product/skuinfo/info/{skuId}")
    public R info(@PathVariable("skuId") Long skuId);


}
