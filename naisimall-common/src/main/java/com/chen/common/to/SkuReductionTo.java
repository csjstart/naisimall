package com.chen.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author chenouba
 * @create 2021-07-24 20:02
 */
@Data
public class SkuReductionTo {

    private Long skuId;

    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;

    private List<MemberPrice> memberPrice;


}
