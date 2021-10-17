package xyz.klenkiven.kmall.ware.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import xyz.klenkiven.kmall.common.utils.R;

/**
 * SKU information RPC
 * @author klenkiven
 * @email wzl709@outlook.com
 */
@FeignClient("kmall-product")
public interface SkuFeignService {

    @GetMapping("/product/skuinfo/info/{skuId}")
    R infoSku(@PathVariable Long skuId);

}
