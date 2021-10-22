package xyz.klenkiven.kmall.product.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import xyz.klenkiven.kmall.common.to.SkuHasStockTO;
import xyz.klenkiven.kmall.common.utils.R;

import java.util.List;

/**
 * Ware Feign RPC
 * @author klenkiven
 */
@FeignClient("kmall-ware")
public interface WareFeignService {

    /**
     * [RPC] Query SKU has Stock
     * /ware/waresku/has-stock
     */
    @PostMapping("/ware/waresku/has-stock")
    R<List<SkuHasStockTO>> getSkuHasStock(@RequestBody List<Long> skuIds);

}
