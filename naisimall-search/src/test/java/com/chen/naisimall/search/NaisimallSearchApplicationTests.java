package com.chen.naisimall.search;


import com.alibaba.fastjson.JSON;
import com.chen.naisimall.search.config.NaisimallElasticSearchConfig;
import lombok.Data;
import lombok.ToString;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NaisimallSearchApplicationTests {
    @Data
    @ToString
    static class Accout {

        private int account_number;
        private int balance;
        private String firstname;
        private String lastname;
        private int age;
        private String gender;
        private String address;
        private String employer;
        private String email;
        private String city;
        private String state;


    }

    /**
     * {
     *     skuId：1
     *     spuId：11
     *     skuTitle：华为xx
     *     price：998
     *     saleCount:99
     *     attrs:[
     *          {尺寸：5寸}，
     *          {CPU：高通985}，
     *          {分辨率：全高清}
     *     ]
     * }
     * 冗余字段
     *
     */

    @Qualifier("esRestClient")
    @Autowired
    private RestHighLevelClient client;
    /**
     *
     */
    @Test
    public void searchData() throws IOException {
        //创建检索请求
        SearchRequest searchRequest = new SearchRequest();
        //指定索引
        searchRequest.indices("bank");
        //指定DSL
        //SearchSourceBuilder sourceBuilder封装的条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //1.1构造检索条件
        sourceBuilder.query(QueryBuilders.matchQuery("address","mill"));

        //1.2按照年龄的值分布进行聚合
        TermsAggregationBuilder ageAgg = AggregationBuilders.terms("ageAgg").field("age").size(10);
        sourceBuilder.aggregation(ageAgg);
        //1.3计算平均薪资
        AvgAggregationBuilder balanceAvg = AggregationBuilders.avg("balanceAvg").field("balance");
        sourceBuilder.aggregation(balanceAvg);

        System.out.println("检索条件:" + sourceBuilder.toString());

        searchRequest.source(sourceBuilder);

        //2,执行检索
        SearchResponse searchResponse = client.search(searchRequest, NaisimallElasticSearchConfig.COMMON_OPTIONS);

        //3,分析结果
        System.out.println(searchResponse.toString());
        Map map = JSON.parseObject(searchResponse.toString(), Map.class);
        //3.1获取所有查到的数据
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            String string = hit.getSourceAsString();
            Accout accout = JSON.parseObject(string, Accout.class);
            System.out.println("accout:"+accout);
        }

        //3.2获取这次检索到的分析数据
        Aggregations aggregations = searchResponse.getAggregations();
        /*for (Aggregation aggregation : aggregations.asList()) {
            System.out.println("当前聚合:" + aggregation.getName());

        }*/
        Terms ageAvg1 = aggregations.get("ageAgg");
        for (Terms.Bucket bucket : ageAvg1.getBuckets()) {
            String keyAsString = bucket.getKeyAsString();
            System.out.println("年龄:"+keyAsString+"===>"+bucket.getDocCount());
        }

        Avg balanceAvg1 = aggregations.get("balanceAvg");
        System.out.println("平均薪资:"+balanceAvg1.getValue());

    }
    /**
     * 测试存储数据到es
     */
    @Test
    public void indexData() throws IOException {
        IndexRequest indexRequest = new IndexRequest("users");
        indexRequest.id("1");
//        indexRequest.source("userName","zhangsan","age",18,"gender","男");
        User user = new User();
        user.setAge(18);
        user.setUserName("zhangsan");
        user.setGender("男");
        String jsonString = JSON.toJSONString(user);
        //要保存的数据
        indexRequest.source(jsonString, XContentType.JSON);

        //执行操作
        IndexResponse index = client.index(indexRequest, NaisimallElasticSearchConfig.COMMON_OPTIONS);
        //提取有用的响应数据
        System.out.println(index);
    }
    @Data
    class User{
        private String userName;
        private String gender;
        private Integer age;
    }

    @Test
    public void contextLoads() {
        System.out.println(client);
    }

}