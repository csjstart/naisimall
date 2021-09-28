package com.chen.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author chenouba
 * @create 2021-07-24 19:43
 */
@Data
public class SpuBoundTo {
    private Long spuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;
}
