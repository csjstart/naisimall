package com.chen.naisimall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @author chenouba
 * @create 2021-07-26 22:52
 */
@Data
public class MergeVo {

    /**
     * 整单ID
     */
    private Long purchaseId;


    private List<Long> items;
}
