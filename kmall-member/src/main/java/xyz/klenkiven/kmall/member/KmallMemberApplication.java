package xyz.klenkiven.kmall.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 酷商城-会员管理服务
 */
@SpringBootApplication
@EnableDiscoveryClient
public class KmallMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(KmallMemberApplication.class, args);
    }

}
