package xyz.klenkiven.kmall.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import xyz.klenkiven.kmall.product.service.BrandService;

import java.util.Arrays;
import java.util.UUID;

@SpringBootTest
class KmallProductApplicationTests {

    @Autowired
    BrandService brandService;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Test
    void contextLoads() {
        System.out.println(Arrays.toString(applicationContext.getBeanDefinitionNames()));
    }

    @Test
    void testStringRedis() {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set("hello", "world_" + UUID.randomUUID());
        System.out.println("Saved Value: " + ops.get("hello"));
    }
}
