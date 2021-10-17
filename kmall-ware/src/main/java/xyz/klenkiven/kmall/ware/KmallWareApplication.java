package xyz.klenkiven.kmall.ware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 酷商城-仓储服务
 * @author klenkiven
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class KmallWareApplication {

    public static void main(String[] args) {
        SpringApplication.run(KmallWareApplication.class, args);
    }

}
