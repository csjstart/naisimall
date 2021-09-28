package com.chen.naisimall.search.service;

import com.chen.common.to.es.SkuEsModel;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author woita
 */
@Service
public interface ProductSaveService {
    /**
     * 上架商品
     * @param skuEsModels
     * @throws IOException
     */
    boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;
}
