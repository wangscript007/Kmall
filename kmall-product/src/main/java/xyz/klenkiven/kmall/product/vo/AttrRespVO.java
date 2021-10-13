package xyz.klenkiven.kmall.product.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Attribute For Response VO
 * @author klenkiven
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AttrRespVO extends AttrVO {
    /**
     * Category Name
     */
    private String catelogName;
    /**
     * Attribute Group Name
     */
    private String groupName;
}
