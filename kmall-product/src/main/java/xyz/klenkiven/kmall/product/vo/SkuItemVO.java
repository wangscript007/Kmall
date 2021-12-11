package xyz.klenkiven.kmall.product.vo;

import lombok.Data;
import xyz.klenkiven.kmall.product.entity.SkuImagesEntity;
import xyz.klenkiven.kmall.product.entity.SkuInfoEntity;
import xyz.klenkiven.kmall.product.entity.SpuInfoDescEntity;

import java.util.List;

/**
 * Sku Item VO
 * @author klenkiven
 */
@Data
public class SkuItemVO {

    /**
     * SKU basic info `pms_sku_info`
     */
    SkuInfoEntity info;

    /**
     * Has Stock
     */
    Boolean hasStock = true;

    /**
     * SKU images `pms_sku_images`
     */
     private List<SkuImagesEntity> images;

    /**
     * SPU sale attribute combination
     */
    private List<SkuItemSaleAttrVO> saleAttrs;

    /**
     * SPU detail information (Description)
     */
    private SpuInfoDescEntity description;

    /**
     * SPU specification attribute info
     */
    private List<SpuItemBaseGroupAttrVO> groupAttrs;


    /**
     * SKU Sale Attribute
     */
    @Data
    public static class SkuItemSaleAttrVO {
        /**
         * 属性id
         */
        private Long attrId;
        /**
         * 属性名
         */
        private String attrName;
        /**
         * 属性值
         */
        private List<SaleAttrValue> attrValues;
    }

    @Data
    public static class SaleAttrValue {
        private String attrValue;
        private String skuIds;
    }

    /**
     * SPU Base Attribute
     */
    @Data
    public static class SpuItemBaseGroupAttrVO {
        private String groupName;
        private List<SpuBaseAttrVO> baseAttrs;
    }

    @Data
    public static class SpuBaseAttrVO {
        private String attrName;
        private String attrValue;
    }

}
