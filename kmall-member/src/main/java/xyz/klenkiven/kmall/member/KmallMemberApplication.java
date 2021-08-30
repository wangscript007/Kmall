package xyz.klenkiven.kmall.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 酷商城-会员管理服务
 * @author klenkiven
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"xyz.klenkiven.kmall.member.feign"})
public class KmallMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(KmallMemberApplication.class, args);
    }

}
