package com.chen.common.to;

import lombok.Data;

import java.math.BigDecimal;


/**
 * @author chenshengjie
 * @date 2021年7月24日 16:02:40
 */
@Data
public class MemberPrice {

    private Long id;
    private String name;
    private BigDecimal price;

}