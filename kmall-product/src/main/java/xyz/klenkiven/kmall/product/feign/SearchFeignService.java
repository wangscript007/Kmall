package xyz.klenkiven.kmall.product.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import xyz.klenkiven.kmall.common.to.elasticsearch.SkuESModel;
import xyz.klenkiven.kmall.common.utils.Result;

import java.util.List;

/**
 * Search RPC Service
 */
@FeignClient("kmall-search")
public interface SearchFeignService {

    /**
     * Save Product in ElasticSearch
     */
    @PostMapping("/search/save/product")
    Result<?> productStatusUp(@RequestBody List<SkuESModel> skuESModelList);

}
