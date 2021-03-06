package com.chen.naisimall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.chen.common.to.es.SkuEsModel;
import com.chen.common.utils.R;
import com.chen.naisimall.search.config.NaisimallElasticSearchConfig;
import com.chen.naisimall.search.constant.EsConstant;
import com.chen.naisimall.search.feign.ProductFeignService;
import com.chen.naisimall.search.service.MallSearchService;
import com.chen.naisimall.search.vo.AttrResponseVo;
import com.chen.naisimall.search.vo.BrandVo;
import com.chen.naisimall.search.vo.SearchParam;
import com.chen.naisimall.search.vo.SearchResult;
import org.apache.lucene.search.TotalHits;
import org.apache.lucene.search.join.ScoreMode;
import org.apache.tomcat.util.security.Escape;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.AggregatorFactories;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ????????????
 *
 * @author woita
 */
@Service
public class MallSearchServiceImpl implements MallSearchService {

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    ProductFeignService productFeignService;

    @Override
    public SearchResult search(SearchParam param) {
        //1,??????????????????????????????DSL??????
        SearchResult result = null;
        //2,??????????????????
        SearchRequest searchRequest = buildSearchRequest(param);
        try {
            //2,??????????????????
            SearchResponse response = client.search(searchRequest, NaisimallElasticSearchConfig.COMMON_OPTIONS);

            //3,??????????????????,??????????????????????????????
            result = buildSearchResult(response, param);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * ??????????????????
     *
     * @param response
     * @return
     */
    private SearchResult buildSearchResult(SearchResponse response, SearchParam param) {
        SearchResult result = new SearchResult();
        //1,????????????????????????????????????
        SearchHits hits = response.getHits();
        List<SkuEsModel> esModels = new ArrayList<>();
        if (hits.getHits() != null && hits.getHits().length > 0) {
            for (SearchHit hit : hits.getHits()) {
                String sourceAsString = hit.getSourceAsString();
                SkuEsModel esModel = JSON.parseObject(sourceAsString, SkuEsModel.class);
                if (!StringUtils.isEmpty(param.getKeyword())){
                    HighlightField skuTitle = hit.getHighlightFields().get("skuTitle");
                    String string = skuTitle.getFragments()[0].string();
                    esModel.setSkuTitle(string);
                }

                esModels.add(esModel);
            }
        }
        result.setProducts(esModels);
        //2,????????????????????????????????????????????????
        List<SearchResult.AttrVo> attrVos = new ArrayList<>();
        ParsedNested attr_agg = response.getAggregations().get("attr_agg");
        ParsedLongTerms attr_id_agg = attr_agg.getAggregations().get("attr_id_agg");
        for (Terms.Bucket bucket : attr_id_agg.getBuckets()) {
            SearchResult.AttrVo attrVo = new SearchResult.AttrVo();
            //1,???????????????ID
            long attrId = bucket.getKeyAsNumber().longValue();
            //2,?????????????????????
            String attrName = ((ParsedStringTerms) bucket.getAggregations().get("attr_name_agg")).getBuckets().get(0).getKeyAsString();
            //3,????????????????????????
            List<String> attrValues = ((ParsedStringTerms) bucket.getAggregations().get("attr_value_agg")).getBuckets().stream().map(item -> {
                String keyAsString = item.getKeyAsString();
                return keyAsString;
            }).collect(Collectors.toList());
            attrVo.setAttrId(attrId);
            attrVo.setAttrName(attrName);
            attrVo.setAttrValue(attrValues);

            attrVos.add(attrVo);
        }


        result.setAttrs(attrVos);

        //3,????????????????????????????????????????????????
        List<SearchResult.BrandVo> brandVos = new ArrayList<>();
        ParsedLongTerms brand_agg = response.getAggregations().get("brand_agg");
        for (Terms.Bucket bucket : brand_agg.getBuckets()) {
            SearchResult.BrandVo brandVo = new SearchResult.BrandVo();
            //1,???????????????ID
            long brandId = bucket.getKeyAsNumber().longValue();
            //2,?????????????????????
            String brandName = ((ParsedStringTerms) bucket.getAggregations().get("brand_name_agg")).getBuckets().get(0).getKeyAsString();
            //3,?????????????????????
            String brandImg = ((ParsedStringTerms) bucket.getAggregations().get("brand_img_agg")).getBuckets().get(0).getKeyAsString();
            brandVo.setBrandId(brandId);
            brandVo.setBrandImg(brandImg);
            brandVo.setBrandName(brandName);


            brandVos.add(brandVo);
        }
        result.setBrands(brandVos);
        //4,????????????????????????????????????????????????
        ParsedLongTerms catelog_agg = response.getAggregations().get("catelog_agg");
        List<SearchResult.CatelogVo> catelogVos = new ArrayList<>();
        List<? extends Terms.Bucket> buckets = catelog_agg.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            SearchResult.CatelogVo catelogVo = new SearchResult.CatelogVo();
            //????????????ID
            String keyAsString = bucket.getKeyAsString();
            catelogVo.setCatelogId(Long.parseLong(keyAsString));

            //???????????????
            ParsedStringTerms catelog_name_agg = bucket.getAggregations().get("catelog_name_agg");
            String catelog_name = catelog_name_agg.getBuckets().get(0).getKeyAsString();
            catelogVo.setCatelogName(catelog_name);
            catelogVos.add(catelogVo);
        }
        result.setCatelogs(catelogVos);
        //========??????????????????????????????
        //5,????????????-??????
        result.setPageNum(param.getPageNum());
        //6????????????
        long total = hits.getTotalHits().value;
        result.setTotal(total);
        //7,????????????-???????????????
        int totalPages = total % EsConstant.PRODUCT_PAGE_SIZE == 0 ?
                (int) total / EsConstant.PRODUCT_PAGE_SIZE : (int) (total / EsConstant.PRODUCT_PAGE_SIZE + 1);
        result.setTotalPages(totalPages);
        List<Integer> pageNavs = new ArrayList<>();
        for (int i=1;i<=totalPages;i++){
            pageNavs.add(i);
        }
        result.setPageNavs(pageNavs);

        //6,???????????????????????????

        //List<SearchResult.NavVo> navVo = new ArrayList<>();
        if (param.getAttrs() != null && param.getAttrs().size() > 0){
            List<SearchResult.NavVo> collect = param.getAttrs().stream().map(attr -> {
                //1,????????????attr???????????????????????????
                SearchResult.NavVo navVo = new SearchResult.NavVo();
                //attrs=2_??????:6???
                String[] s = attr.split("_");
                navVo.setNavValue(s[1]);

                R r = productFeignService.attrInfo(Long.parseLong(s[0]));
                result.getAttrIds().add(Long.parseLong(s[0]));
                if (r.getCode() == 0){
                    AttrResponseVo data = r.getData("attr", new TypeReference<AttrResponseVo>() {
                    });
                    navVo.setNavName(data.getAttrName());
                }else {
                    navVo.setNavName(s[0]);
                }
                //2,??????????????????????????????,??????????????????????????????,??????????????????url?????????????????????
                //???????????????????????????,????????????
                String replace = replaceQueryString(param, attr,"attrs");
                navVo.setLink("http://search.naisimall.com/list.html?"+replace);
                return navVo;
            }).collect(Collectors.toList());

            result.setNavs(collect);
        }

        //??????,??????
        if (param.getBrandId() != null && param.getBrandId().size() > 0){
            List<SearchResult.NavVo> navs = result.getNavs();
            SearchResult.NavVo navVo = new SearchResult.NavVo();
            navVo.setNavName("??????");
            //TODO ????????????????????????
            R r = productFeignService.brandsInfo(param.getBrandId());
            if (r.getCode() == 0){
                List<BrandVo> brand = r.getData("brand", new TypeReference<List<BrandVo>>() {
                });
                StringBuffer buffer = new StringBuffer();
                String replace = "";
                for (BrandVo brandVo : brand) {
                    buffer.append(brandVo.getBrandName()+";");
                    replace = replaceQueryString(param,brandVo.getBrandId()+"","brandId");
                }
                navVo.setNavValue(buffer.toString());
                navVo.setLink("http://search.naisimall.com/list.html?"+replace);

            }
            navs.add(navVo);
        }
        //TODO ??????:?????????????????????


        return result;
    }

    private String replaceQueryString(SearchParam param, String value,String key) {
        String encode = null;
        try {
            encode = URLEncoder.encode(value, "UTF-8");
            encode = encode.replace("+","%20")
                    .replace("%29",")").replace("%28","(");//???????????????????????????java?????????
        } catch (Exception e) {
            e.printStackTrace();
        }
        String replace = param.get_queryString().replace("&"+key+"="+ encode, "");
        return replace;
    }

    /**
     * ??????????????????
     * #????????????,??????(????????????,??????,??????,????????????,??????),??????,??????,??????,????????????
     *
     * @return
     */
    private SearchRequest buildSearchRequest(SearchParam param) {

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();//??????DSL?????????

        /**
         * ????????????,??????(????????????,??????,??????,????????????,??????),
         */
        //1,?????????bool - query
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //1.1,must
        if (!StringUtils.isEmpty(param.getKeyword())) {
            boolQuery.must(QueryBuilders.matchQuery("skuTitle", param.getKeyword()));
        }
        //1.2,bool - filter - ??????????????????ID??????
        if (param.getCatelog3Id() != null) {
            boolQuery.filter(QueryBuilders.termQuery("catelogId", param.getCatelog3Id()));
        }
        //1.2,bool - filter - ????????????ID??????
        if (param.getBrandId() != null && param.getBrandId().size() > 0) {
            boolQuery.filter(QueryBuilders.termsQuery("brandId", param.getBrandId()));
        }
        //1.2,bool - filter - ??????????????????
        if (!StringUtils.isEmpty(param.getSkuPrice())) {
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("skuPrice");
            String[] s = param.getSkuPrice().split("_");
            if (s.length == 2) {
                //??????
                rangeQuery.gte(s[0]).lte(s[1]);
            } else if (s.length == 1) {
                if (param.getSkuPrice().startsWith("_")) {
                    rangeQuery.lte(s[0]);
                }
                if (param.getSkuPrice().endsWith("_")) {
                    rangeQuery.gte(s[0]);
                }
            }
            boolQuery.filter(rangeQuery);
        }

        //1.2,bool - filter - ???????????????????????????????????????
        if (param.getAttrs() != null && param.getAttrs().size() > 0) {

            for (String attrStr : param.getAttrs()) {
                //attrs=1_5???:8???
                BoolQueryBuilder nestedboolQuery = QueryBuilders.boolQuery();
                String[] s = attrStr.split("_");
                String attrId = s[0];//???????????????ID
                String[] attrValues = s[1].split(":");//??????????????????????????????
                nestedboolQuery.must(QueryBuilders.termQuery("attrs.attrId", attrId));
                nestedboolQuery.must(QueryBuilders.termsQuery("attrs.attrValue", attrValues));
                //?????????????????????????????????nested??????
                NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("attrs", nestedboolQuery, ScoreMode.None);
                boolQuery.filter(nestedQuery);
            }

        }
        //1.2,bool - filter - ?????????????????????????????????
        if (param.getHasStock() != null){
            boolQuery.filter(QueryBuilders.termQuery("hasStock", param.getHasStock() == 1));
        }

        //?????????????????????????????????????????????
        sourceBuilder.query(boolQuery);
        /**
         * ??????,??????,??????,
         */
        //2.1,??????
        if (!StringUtils.isEmpty(param.getSort())) {
            String sort = param.getSort();
            String[] s = sort.split("_");
            SortOrder order = s[1].equalsIgnoreCase("asc") ? SortOrder.ASC : SortOrder.DESC;
            sourceBuilder.sort(s[0], order);
        }
        //2.2?????? pageSize = 5
        //from = (pageNum - 1) * size
        sourceBuilder.from((param.getPageNum() - 1) * EsConstant.PRODUCT_PAGE_SIZE);
        sourceBuilder.size(EsConstant.PRODUCT_PAGE_SIZE);

        //2.3,??????
        if (!StringUtils.isEmpty(param.getKeyword())) {
            HighlightBuilder builder = new HighlightBuilder();
            builder.field("skuTitle");
            builder.preTags("<b style='color:red'>");
            builder.postTags("</b>");
            sourceBuilder.highlighter(builder);
        }


        /**
         * ????????????
         */
        //1,????????????
        TermsAggregationBuilder brand_agg = AggregationBuilders.terms("brand_agg");
        brand_agg.field("brandId").size(50);
        //????????????????????????
        brand_agg.subAggregation(AggregationBuilders.terms("brand_name_agg").field("brandName").size(1));
        brand_agg.subAggregation(AggregationBuilders.terms("brand_img_agg").field("brandImg").size(1));
        //TODO 1,??????brand
        sourceBuilder.aggregation(brand_agg);
        //2,???????????? catelog_agg
        TermsAggregationBuilder catelog_agg = AggregationBuilders.terms("catelog_agg").field("catelogId").size(20);
        catelog_agg.subAggregation(AggregationBuilders.terms("catelog_name_agg").field("catelogName").size(1));
        //TODO 2,??????catelog
        sourceBuilder.aggregation(catelog_agg);

        //3,????????????
        NestedAggregationBuilder attr_agg = AggregationBuilders.nested("attr_agg", "attrs");
        //????????????????????????attr_id
        TermsAggregationBuilder attr_id_agg = AggregationBuilders.terms("attr_id_agg").field("attrs.attrId");
        //?????????????????????attr_id???????????????
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_name_agg").field("attrs.attrName").size(1));
        //?????????????????????attr_id?????????????????????????????????attr_value
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_value_agg").field("attrs.attrValue").size(50));
        attr_agg.subAggregation(attr_id_agg);
        //TODO 3,??????attr
        sourceBuilder.aggregation(attr_agg);

        String s = sourceBuilder.toString();
        System.out.println("?????????DSL??????" + s);

        SearchRequest searchRequest = new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX}, sourceBuilder);

        return searchRequest;
    }
}
