package xyz.klenkiven.kmall.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 酷商城-优惠券
 * @author klenkiven
 */
@SpringBootApplication
@EnableDiscoveryClient
public class KmallCouponApplication {

    public static void main(String[] args) {
        SpringApplication.run(KmallCouponApplication.class, args);
    }

}
