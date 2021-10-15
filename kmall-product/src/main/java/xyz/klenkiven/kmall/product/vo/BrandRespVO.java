package xyz.klenkiven.kmall.product.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * Category and Brand Relation for Response VO
 * @author klenkiven
 */
@Data
public class BrandRespVO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long brandId;

    private String brandName;

}
