package xyz.klenkiven.kmall.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 酷商城-商品服务
 * @author klenkiven
 */
@SpringBootApplication
@EnableDiscoveryClient
public class KmallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(KmallProductApplication.class, args);
    }

}
