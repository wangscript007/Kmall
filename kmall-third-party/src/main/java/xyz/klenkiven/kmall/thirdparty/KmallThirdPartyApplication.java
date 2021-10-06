package xyz.klenkiven.kmall.thirdparty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Kmall Third Party Service
 * @author klenkiven
 */
@EnableDiscoveryClient
@SpringBootApplication
public class KmallThirdPartyApplication {

    public static void main(String[] args) {
        SpringApplication.run(KmallThirdPartyApplication.class, args);
    }

}
