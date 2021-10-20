package xyz.klenkiven.kmall.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Kmall ElasticSearch Service
 * @author klenkiven
 * @email wzl709@outlook.com
 */
@EnableDiscoveryClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class KmallSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(KmallSearchApplication.class, args);
    }

}
