package com.chen.naisimall.cart.vo;

import lombok.Data;
import lombok.ToString;

/**
 * User登录信息
 * @author woita
 */
@ToString
@Data
public class UserInfoTo {

    private Long userId;
    /**
     * 一定封装
     */
    private String userKey;

    private boolean tempUser = false;

}
