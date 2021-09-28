/**
 * Copyright 2021 bejson.com
 */
package com.chen.naisimall.product.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Auto-generated: 2021-07-24 15:27:24
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
public class SpuSaveVo {

    private String spuName;
    private String spuDescription;
    private Long catelogId;
    private Long brandId;
    private BigDecimal weight;
    private int publishStatus;
    private List<String> decript;
    private List<String> images;
    private Bounds bounds;
    private List<BaseAttrs> baseAttrs;
    private List<Skus> skus;


}