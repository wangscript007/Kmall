package xyz.klenkiven.kmall.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import xyz.klenkiven.kmall.product.service.BrandService;

import java.util.Arrays;

@SpringBootTest
class KmallProductApplicationTests {

    @Autowired
    BrandService brandService;

    @Autowired
    ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        System.out.println(Arrays.toString(applicationContext.getBeanDefinitionNames()));
    }

}
