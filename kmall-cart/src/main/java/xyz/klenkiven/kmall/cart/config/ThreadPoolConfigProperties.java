package xyz.klenkiven.kmall.cart.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "kmall.thread")
@Component
@Data
public class ThreadPoolConfigProperties {

    private Integer idle;

    private Integer maxSize;

    private Integer keepAlive;

}
