/**
 * Copyright 2021 bejson.com
 */
package com.chen.naisimall.product.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author chenshengjie
 * @date 2021年7月24日 15:59:59
 *
 */
@Data
public class Skus {

    private List<Attr> attr;
    private String skuName;
    private BigDecimal price;
    private String skuTitle;
    private String skuSubtitle;
    private List<Images> images;
    private List<String> descar;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
    private List<MemberPrice> memberPrice;
}