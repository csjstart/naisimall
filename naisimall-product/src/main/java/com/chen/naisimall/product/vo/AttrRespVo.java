package com.chen.naisimall.product.vo;

import lombok.Data;

/**
 * @author chenouba
 * @create 2021-06-27 20:08
 */
@Data
public class AttrRespVo extends AttrVo {

    /**
     * "catelogName": "手机/数码/手机",   //所属分类名字
     * "groupName": "主体",       //所属分组名字
     */

    private String catelogName;

    private String groupName;

    private Long[] catelogPath;
}
