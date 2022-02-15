package xyz.klenkiven.kmall.cart.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.klenkiven.kmall.common.utils.R;
import xyz.klenkiven.kmall.common.utils.Result;

import java.util.List;

/**
 * Remote HTTP Feign Client
 * @author klenkiven
 */
@FeignClient("kmall-product")
public interface ProductFeignService {

    /**
     * SKU 信息
     */
    @RequestMapping("/product/skuinfo/info/{skuId}")
    public R getSkuInfo(@PathVariable("skuId") Long skuId);

    /**
     * Get SKU Sale Attribute as List
     */
    @GetMapping("/product/skusaleattrvalue/stringList/{skuId}")
    public Result<List<String>> skuSaleAttrStringList(@PathVariable Long skuId);

}
