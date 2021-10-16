package xyz.klenkiven.kmall.product.vo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Auto-generated: 2021-10-15 20:45:48
 *
 * @author bejson.com (i@bejson.com)
 */
@Data
public class Skus {

    private List<Attr> attr;

    private String skuName;

    private BigDecimal price;

    private String skuTitle;

    private String skuSubtitle;

    private List<Images> images;

    private List<String> descar;

    private int fullCount;

    private BigDecimal discount;

    /**
     * Ladder Count Status
     */
    private int countStatus;

    private BigDecimal fullPrice;

    private BigDecimal reducePrice;

    /**
     * Full Reduction Status
     */
    private int priceStatus;

    private List<MemberPrice> memberPrice;

}