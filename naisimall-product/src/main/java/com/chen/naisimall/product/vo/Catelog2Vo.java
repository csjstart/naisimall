package com.chen.naisimall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author chenshengjie
 * 二级分类Vo
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Catelog2Vo {

    /**
     * 一级父分类ID
     */
    private String catelog1Id;

    /**
     * 三级子分类
     */
    private List<catelog3Vo> catelog3List;

    private String id;

    private String name;


    /**
     * 三级分类Vo
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class catelog3Vo{
        private String catelog2Id;
        private String id;
        private String name;
    }
}
