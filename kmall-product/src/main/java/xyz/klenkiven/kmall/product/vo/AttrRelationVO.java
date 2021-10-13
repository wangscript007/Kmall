package xyz.klenkiven.kmall.product.vo;

import lombok.Data;

/**
 * Attribution Relation VO
 * <p>Used to Attribute Group delete relation in batch.</p>
 * @author klenkiven
 */
@Data
public class AttrRelationVO {
    /**
     * Attribute ID
     */
    private Long attrId;
    /**
     * Attribute Group ID
     */
    private Long attrGroupId;
}
