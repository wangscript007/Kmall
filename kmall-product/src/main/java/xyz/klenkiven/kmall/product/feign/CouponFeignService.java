package xyz.klenkiven.kmall.product.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import xyz.klenkiven.kmall.common.to.SkuReductionTO;
import xyz.klenkiven.kmall.common.to.SpuBoundsTO;
import xyz.klenkiven.kmall.common.utils.R;

/**
 * Open Feign RPC
 * @author klenkiven
 */
@FeignClient("kmall-coupon")
public interface CouponFeignService {

    /**
     * [RPC] Save Spu Bounds
     */
    @PostMapping("/coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundsTO spuBounds);

    /**
     * Save All Sku Reduction
     */
    @PostMapping("/coupon/skufullreduction/save/all-reduction")
    R saveSkuReduction(@RequestBody SkuReductionTO skuReductionTO);

}
