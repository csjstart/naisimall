package com.chen.naisimall.search.controller;

import com.chen.common.exception.BizCodeEnume;
import com.chen.common.to.es.SkuEsModel;
import com.chen.common.utils.R;
import com.chen.naisimall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author woita
 */
@Slf4j
@RequestMapping("/search/save")
@RestController
public class ElasticSaveController {

    @Autowired
    ProductSaveService productSaveService;

    /**
     * @param skuEsModels
     * 上架商品
     */
    @PostMapping("/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels){

        boolean b = false;
        try {
            b = productSaveService.productStatusUp(skuEsModels);
        }catch (Exception e){
            log.error("ElasticSaveController商品上架错误:{}",e);
            return R.error(BizCodeEnume.PRUDUCT_UP_EXCEPTION.getCode(),BizCodeEnume.PRUDUCT_UP_EXCEPTION.getMsg());
        }
        if (!b){
            return R.ok();
        }else {
            return R.error(BizCodeEnume.PRUDUCT_UP_EXCEPTION.getCode(),BizCodeEnume.PRUDUCT_UP_EXCEPTION.getMsg());
        }

    }

}
