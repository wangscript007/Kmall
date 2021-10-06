package xyz.klenkiven.kmall.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import xyz.klenkiven.kmall.product.entity.BrandEntity;
import xyz.klenkiven.kmall.product.service.BrandService;

import java.io.ByteArrayInputStream;

@SpringBootTest
class KmallProductApplicationTests {

    @Autowired
    BrandService brandService;

    @Autowired
    ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setName("KlenKiven");
        // brandService.save(brandEntity);
    }

}
