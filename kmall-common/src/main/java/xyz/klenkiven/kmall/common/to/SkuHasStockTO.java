package xyz.klenkiven.kmall.common.to;

import lombok.Data;

/**
 * [RPC] SKU Has Stock TO
 * @author klenkiven
 */
@Data
public class SkuHasStockTO {

    private Long skuId;

    private Boolean hasStock;

}
