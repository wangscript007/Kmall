package xyz.klenkiven.kmall.search.vo;

import lombok.Data;
import xyz.klenkiven.kmall.common.to.elasticsearch.SkuESModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Search Result from ES
 * @author klenkiven
 */
@Data
public class SearchResult {

    private List<SkuESModel> products;// es检索到的所有商品信息

    /**
     * 分页信息
     */
    private Integer pageNum;// 当前页码
    private Long total;// 总记录数
    private Integer totalPages;// 总页码
    private List<Integer> pageNavs;// 允许的页数集合[1、2、3、4、5]

    private List<BrandVo> brands;// 当前查询到的结果所有涉及到的品牌
    private List<CatalogVo> catalogs;// 当前查询到的结果所有涉及到的分类
    /**
     * attrs=1_anzhuo&attrs=5_其他:1080P
     */
    private List<AttrVo> attrs;// 当前查询到的结果所有涉及到的属性【符合检索条件的，可检索的属性】


    // ============================以上是要返回的数据====================================

    //面包屑导航数据
    private List<NavVo> navs = new ArrayList<>();
    private List<Long> attrIds = new ArrayList<>();


    @Data
    public static class NavVo {
        private String navName;// 属性名
        private String navValue;//属性值
        private String link;// 取消了之后要调到那个地方
    }

    @Data
    public static class BrandVo {
        private Long brandId;//
        private String brandName;//
        private String brandImg;//
    }

    @Data
    public static class CatalogVo {
        private Long catalogId;//
        private String catalogName;//
    }

    @Data
    public static class AttrVo {
        private Long attrId;// 允许检索的 属性Id
        private String attrName;// 允许检索的 属性名
        private List<String> attrValue;// 属性值【多个】
    }
}