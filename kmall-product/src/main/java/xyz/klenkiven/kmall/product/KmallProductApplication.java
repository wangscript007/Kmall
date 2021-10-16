package xyz.klenkiven.kmall.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 酷商城-商品服务
 * @author klenkiven
 */
@EnableFeignClients
@SpringBootApplication(scanBasePackages = {"xyz.klenkiven.kmall.*"})
@EnableDiscoveryClient
public class KmallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(KmallProductApplication.class, args);
    }

}
