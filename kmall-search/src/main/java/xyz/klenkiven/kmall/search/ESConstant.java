package xyz.klenkiven.kmall.search;

/**
 * ElasticSearch - Index Constant
 * @author klenkiven
 */
public final class ESConstant {
    private ESConstant() { }

    /**
     * Product Service Index
     */
    public static final String PRODUCT_INDEX = "k_product";

    /**
     * ES Query Page Size
     */
    public static final Integer SEARCH_PAGE_SIZE = 16;
}
