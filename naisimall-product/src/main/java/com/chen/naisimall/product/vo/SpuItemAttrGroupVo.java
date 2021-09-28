package com.chen.naisimall.product.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;


/**
 * spu基本属性
 */
@ToString
@Data
public class SpuItemAttrGroupVo {
    private String groupName;
    private List<Attr> attrs;
}
