package com.chen.naisimall.ware.vo;

import lombok.Data;

/**
 * @author woita
 */

@Data
public class PurchaseItemDoneVo {
    /**
     * "itemId":5,"status":3,"reason":""
     */
    private Long itemId;

    private Integer status;

    private String reason;
}
