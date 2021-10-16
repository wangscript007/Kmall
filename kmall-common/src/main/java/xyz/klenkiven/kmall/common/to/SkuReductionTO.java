package xyz.klenkiven.kmall.common.to;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * SKU Reduction Information
 * @author klenkiven
 */
@Data
public class SkuReductionTO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long skuId;

    private int fullCount;

    private BigDecimal discount;

    private int countStatus;

    private BigDecimal fullPrice;

    private BigDecimal reducePrice;

    private int priceStatus;

    private List<MemberPrice> memberPrice;

}
