package xyz.klenkiven.kmall.search;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.klenkiven.kmall.search.config.KmallElasticSearchConfig;

import java.io.IOException;

@SpringBootTest
class KmallSearchApplicationTests {

    @Autowired
    private RestHighLevelClient esRestClient;

    @Test
    void contextLoads() {
        System.out.println(esRestClient);
    }

    @Test
    void indexRequestTest() throws IOException {
        IndexRequest indexRequest = new IndexRequest("users");
        // 1. source
        // indexRequest.source("userName", "zhangsan", "age", 18, "gender", "man");

        // 2. Json
        User user = new User("zhangsan", 18, "man");
        String s = JSON.toJSONString(user);
        indexRequest.source(s, XContentType.JSON);

        // Execution
        // 1. Synchronized
        IndexResponse index = esRestClient.index(indexRequest, KmallElasticSearchConfig.COMMON_OPTIONS);
        System.out.println(index);
    }

    @Data
    @AllArgsConstructor
    static class User {
        private String username;
        private Integer age;
        private String gender;
    }



}
