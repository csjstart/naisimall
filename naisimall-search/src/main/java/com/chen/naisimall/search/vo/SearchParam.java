package com.chen.naisimall.search.vo;

import lombok.Data;

import java.util.List;

/**
 * 封装页面所有可能传递过来的查询条件
 * catalog3Id=225&keyword=小米&sort=saleCount_asc&hasStock=1/0
 */
@Data
public class SearchParam {

    private String keyword;//页面传递过来的全文匹配关键字

    private Long catelog3Id;//三级分类ID

    /**
     * sort=saleCount_asc/desc
     * sort=skuPrice_asc/desc
     * sort=hotScore_asc/desc
     */
    private String sort;//排序条件

    /**
     * 好多的过滤条件
     * hasStock(是否有货),skuPrice区间,brandId,catalog3Id,attrs
     * hasStock=0/1
     * skuPrice=1_500/_500/500_
     * brandId=1
     * attrs=2_5寸:6寸
     *
     */

    private Integer hasStock;//是否只显示有货 0(无库存) 1(有库存)

    private String skuPrice;//价格区间

    private List<Long> brandId;//品牌ID,按照品牌进行筛选,可以多选

    private List<String> attrs;//按照属性进行筛选

    private Integer pageNum = 1;//页码

    private String _queryString;//原生查询条件
}
