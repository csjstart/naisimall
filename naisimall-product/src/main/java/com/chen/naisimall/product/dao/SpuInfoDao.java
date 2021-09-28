package com.chen.naisimall.product.dao;

import com.chen.naisimall.product.entity.SpuInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * spu信息
 * @author csjstart
 * @email 1047715631.com
 * @date 2021-05-27 22:02:30
 */
@Mapper
public interface SpuInfoDao extends BaseMapper<SpuInfoEntity> {


    /**
     * 修改spu状态
     * @param spuId
     * @param code
     */
    void updateSpuStatus(@Param("spuId") Long spuId, @Param("code") int code);
}
