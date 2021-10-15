package xyz.klenkiven.kmall.product.vo;
import lombok.Data;

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

    private String price;

    private String skuTitle;

    private String skuSubtitle;

    private List<Images> images;

    private List<String> descar;

    private int fullCount;

    private int discount;

    private int countStatus;

    private int fullPrice;

    private int reducePrice;

    private int priceStatus;

    private List<MemberPrice> memberPrice;

}