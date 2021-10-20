package xyz.klenkiven.kmall.search;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.klenkiven.kmall.search.config.KmallElasticSearchConfig;

import java.io.IOException;
import java.util.List;

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

    @Test
    void testQuery() throws IOException {
        // 1. Create Search Request
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("bank");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // Construct Search Condition
        searchSourceBuilder.query(QueryBuilders.matchQuery("address", "mill"));

        // Aggregation Builders: Age Aggregation
        TermsAggregationBuilder ageAgg = AggregationBuilders.terms("ageAgg").field("age").size(100);
        // Aggregation Builders: Gender Aggregation
        TermsAggregationBuilder genderAgg = AggregationBuilders.terms("genderAgg").field("gender.keyword").size(2);
        // Aggregation Builder: Average Salary Aggregation
        AvgAggregationBuilder balanceAvg = AggregationBuilders.avg("balanceAvg").field("balance");

        ageAgg.subAggregation(genderAgg);
        genderAgg.subAggregation(balanceAvg);
        searchSourceBuilder.aggregation(ageAgg);

        System.out.println(searchSourceBuilder);
        searchRequest.source(searchSourceBuilder);

        // 2. Execution
        SearchResponse search = esRestClient.search(searchRequest, KmallElasticSearchConfig.COMMON_OPTIONS);

        // 3. Analyze
        // 3.1 Search Hits
        SearchHits hits = search.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit searchHit : searchHits) {
            String sourceAsString = searchHit.getSourceAsString();
            Account account = JSON.parseObject(sourceAsString, Account.class);
            System.out.println(account);
        }
        // 3.2 Search Aggregation List
        Aggregations aggregations = search.getAggregations();
        Terms ageAggTerms = aggregations.get("ageAgg");
        for (Terms.Bucket bucket : ageAggTerms.getBuckets()) {
            System.out.println("Age: " + bucket.getKey() + "  ==>  " + bucket.getDocCount());
            Terms gender = bucket.getAggregations().get("genderAgg");
            for (Terms.Bucket genderBucket : gender.getBuckets()) {
                System.out.println("\tGender: " + genderBucket.getKey() + "  ==>  " + genderBucket.getDocCount());
                Avg balance = genderBucket.getAggregations().get("balanceAvg");
                System.out.println("\t\tBalance Average: " + balance.getValue());
            }
        }

    }

    @Data
    static class Account {
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

}
