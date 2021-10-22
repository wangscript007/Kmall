package xyz.klenkiven.kmall.common.to.elasticsearch;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * SKU Model in elasticsearch
 */
@Data
public class SkuESModel {

    /**
     * [SKU Info] SKU ID
     */
    private Long skuId;

    /**
     * [SKU Info] SPU ID
     */
    private Long spuId;

    /**
     * [SKU Info] SKU Title
     */
    private String skuTitle;

    /**
     * [SKU Info] Price -> skuPrice
     */
    private BigDecimal skuPrice;

    /**
     * [SKU Info] SKU Default Img -> skuImg
     */
    private String skuImg;

    /**
     * [SKU Info] Sale Count
     */
    private Long saleCount;

    /**
     * [WARE RPC] Has Stock
     */
    private Boolean hasStock;

    /**
     * [DEFAULT] Hot Score
     */
    private Long hotScore;

    /**
     * [SKU Info] Brand ID
     */
    private Long brandId;

    /**
     * [SKU Info] Catalog ID
     */
    private Long catalogId;

    /**
     * [Brand] Brand Name
     */
    private String brandName;

    /**
     * [Brand] Brand Img
     */
    private String brandImg;

    /**
     * [Catalog] Catalog Name
     */
    private String catalogName;

    /**
     * [SPU Attr]
     */
    private List<Attrs> attrs;

    @Data
    public static class Attrs {

        private Long attrId;

        private String attrName;

        private String attrValue;

    }

}
