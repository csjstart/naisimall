package com.chen.naisimall.ware.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author chenshengjie
 */
@Data
public class PurchaseDoneVo {

    /**
     * 采购单ID
     */
    @NotNull
    private Long id;

    /**
     *
     */
    private List<PurchaseItemDoneVo> items;
}
