package com.chen.naisimall.product.vo;


import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * sku销售属性
 */
@ToString
@Data
public class SkuItemSaleAttrVo {
    private Long attrId;
    private String attrName;
    private List<AttrValueWithSkuIdVo> attrValues;

}
