/**
 * Copyright 2021 bejson.com
 */
package com.chen.naisimall.product.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author chenshengjie
 * @date 2021年7月24日 15:58:13
 * 积分
 */
@Data
public class Bounds {

    private BigDecimal buyBounds;
    private BigDecimal growBounds;

}