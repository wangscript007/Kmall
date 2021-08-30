package xyz.klenkiven.kmall.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 酷商城-网关
 * @author klenkiven
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
public class KmallGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(KmallGatewayApplication.class, args);
    }

}
