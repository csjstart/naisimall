package com.chen.naisimall.search.service;

import com.chen.naisimall.search.vo.SearchParam;
import com.chen.naisimall.search.vo.SearchResult;

public interface MallSearchService {
    /**
     * @param param 检索的所有参数
     * @return 返回检索的结果
     */
    SearchResult search(SearchParam param);
}
