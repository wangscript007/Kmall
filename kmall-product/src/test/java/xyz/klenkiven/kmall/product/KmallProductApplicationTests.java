package xyz.klenkiven.kmall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import xyz.klenkiven.kmall.product.entity.AttrEntity;
import xyz.klenkiven.kmall.product.entity.SkuSaleAttrValueEntity;
import xyz.klenkiven.kmall.product.service.AttrGroupService;
import xyz.klenkiven.kmall.product.service.AttrService;
import xyz.klenkiven.kmall.product.service.BrandService;
import xyz.klenkiven.kmall.product.service.SkuSaleAttrValueService;
import xyz.klenkiven.kmall.product.vo.SkuItemVO;

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

    @Autowired
    AttrGroupService attrGroupService;

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    AttrService attrService;

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

    @Test
    void testGetBaseAttrGroup() {
        for (SkuItemVO.SpuItemBaseGroupAttrVO spuItemBaseGroupAttrVO : attrGroupService.getBaseAttrGroup(26L, 225L)) {
            System.out.println(spuItemBaseGroupAttrVO);
        }

    }

//    @Test
//    void recovery() {
//        for (SkuSaleAttrValueEntity skuSaleAttrValueEntity : skuSaleAttrValueService.list()) {
//            AttrEntity attr_name = attrService.getOne(new QueryWrapper<AttrEntity>().eq("attr_name", skuSaleAttrValueEntity.getAttrName()));
//            skuSaleAttrValueEntity.setAttrId(attr_name.getAttrId());
//            skuSaleAttrValueService.updateById(skuSaleAttrValueEntity);
//        }
//
//    }

    @Test
    void testGetSaleAttr() {
        for (SkuItemVO.SkuItemSaleAttrVO skuItemSaleAttrVO : skuSaleAttrValueService.getSaleAttr(26L)) {
            System.out.println(skuItemSaleAttrVO);
        }

    }
}
