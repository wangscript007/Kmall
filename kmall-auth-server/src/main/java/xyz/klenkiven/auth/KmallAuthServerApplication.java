package xyz.klenkiven.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Kmall Auth Server
 * @author klenkiven
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class KmallAuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(KmallAuthServerApplication.class, args);
    }

}
