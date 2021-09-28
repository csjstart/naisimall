package com.chen.naisimall.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author woita
 * 1,导入依赖
 * 2,修改配置,给容器中注入一个RestHighLevelClient
 * 3,
 */
@Configuration
public class NaisimallElasticSearchConfig {

    @Bean
    public RestHighLevelClient esRestClient(){


//        RestClientBuilder builder = null;
//        builder = RestClient.builder(new HttpHost("192.168.136.88", 9200, "http"));
//        RestHighLevelClient client = new RestHighLevelClient(builder);
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("192.168.136.88", 9200, "http")));
        return client;
    }

    /**
     * 配置请求选项
     * 参考：https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-low-usage-requests.html#java-rest-low-usage-request-options
     */
    public static final RequestOptions COMMON_OPTIONS;

    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        // builder.addHeader("Authorization", "Bearer " + TOKEN);
        // builder.setHttpAsyncResponseConsumerFactory(
        //         new HttpAsyncResponseConsumerFactory
        //                 .HeapBufferedResponseConsumerFactory(30 * 1024 * 1024 * 1024));
        COMMON_OPTIONS = builder.build();
    }

}
