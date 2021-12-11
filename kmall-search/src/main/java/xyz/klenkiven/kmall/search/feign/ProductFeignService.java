package xyz.klenkiven.kmall.search.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.klenkiven.kmall.common.utils.R;
import xyz.klenkiven.kmall.common.utils.Result;
import xyz.klenkiven.kmall.search.vo.BrandVO;

import java.util.List;

/**
 * Product Feign Client
 */
@FeignClient("kmall-product")
public interface ProductFeignService {

    @GetMapping("/product/attr/info/{attrId}")
    R attrInfo(@PathVariable("attrId") Long attrId);

    @GetMapping("/product/brand/infos")
    Result<List<BrandVO>> brandsInfo(@RequestParam("brandIds") List<Long> brandIds);

}
