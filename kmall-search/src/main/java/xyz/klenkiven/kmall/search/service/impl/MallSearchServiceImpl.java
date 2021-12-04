package xyz.klenkiven.kmall.search.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;
import xyz.klenkiven.kmall.common.to.elasticsearch.SkuESModel;
import xyz.klenkiven.kmall.search.ESConstant;
import xyz.klenkiven.kmall.search.config.KmallElasticSearchConfig;
import xyz.klenkiven.kmall.search.service.MallSearchService;
import xyz.klenkiven.kmall.search.vo.SearchParam;
import xyz.klenkiven.kmall.search.vo.SearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mall Search Service Impl
 * @author klenkiven
 */
@Service("mallSearchService")
@RequiredArgsConstructor
public class MallSearchServiceImpl implements MallSearchService {

    private final RestHighLevelClient esClient;

    @Override
    public SearchResult search(SearchParam searchParam) {
        SearchResult result = null;
        // Construct search condition
        SearchRequest searchRequest = buildSearchRequest(searchParam);
        try {
            // Execute the search request
            SearchResponse search = esClient.search(searchRequest, KmallElasticSearchConfig.COMMON_OPTIONS);
            result = buildResult(search, searchParam);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Build Search Request
     * 模糊匹配，过滤（按照分类，品牌，属性，库存，价格区间），排序，分页，高亮
     * 聚合分析
     * @param searchParam search Parameter
     * @return request
     */
    private SearchRequest buildSearchRequest(SearchParam searchParam) {
        SearchRequest searchRequest = new SearchRequest(ESConstant.PRODUCT_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // ======================================模糊匹配 + 过滤=========================================
        BoolQueryBuilder boolQuery = new BoolQueryBuilder();
        // 模糊匹配
        if (!StringUtils.isEmpty(searchParam.getKeyword())) {
            boolQuery.must(new MatchQueryBuilder("skuTitle", searchParam.getKeyword()));
        }

        // 过滤（按照分类，品牌，属性，库存，价格区间）
        // 分类
        if (searchParam.getCatalog3Id() != null) {
            boolQuery.filter(new TermQueryBuilder("catalogId", searchParam.getCatalog3Id()));
        }
        // 品牌
        if (searchParam.getBrandId() != null && searchParam.getBrandId().size() > 0) {
            boolQuery.filter(new TermsQueryBuilder("brandId", searchParam.getBrandId()));
        }
        // 属性
        if (searchParam.getAttrs() != null && searchParam.getAttrs().size() > 0) {
            for (String attr : searchParam.getAttrs()) {
                BoolQueryBuilder nestBoolQuery = new BoolQueryBuilder();
                // attrs = 2_5寸
                String[] s = attr.split("_");
                String attrId = "";
                String[] attrValues = new String[]{};
                if (s.length == 2) {
                    attrId = s[0];
                    boolQuery.must(new TermQueryBuilder("attrs.attrId", attrId));
                    attrValues = s[1].split(":");
                    boolQuery.must(new TermsQueryBuilder("attrs.attrValue", attrValues));
                }

                NestedQueryBuilder nestedQuery = new NestedQueryBuilder("attrs", nestBoolQuery, ScoreMode.None);
                boolQuery.filter(nestedQuery);
            }
        }
        // 库存
        boolQuery.filter(
                new TermQueryBuilder("hasStock", Integer.valueOf(1).equals(searchParam.getHasStock()))
        );

        // 价格区间
        if (!StringUtils.isEmpty(searchParam.getSkuPrice())) {
            // 0_500/500_/_500
            String[] priceRange = searchParam.getSkuPrice().split("_");
            RangeQueryBuilder rangeQuery = new RangeQueryBuilder("skuPrice");
            if (priceRange.length == 2) {
                rangeQuery.gte(priceRange[0]).lte(priceRange[1]);
            } else if (priceRange.length > 0 && searchParam.getSkuPrice().startsWith("_")) {
                rangeQuery.lte(priceRange[0]);
            } else if (priceRange.length > 0) {
                rangeQuery.gte(priceRange[0]);
            }
            boolQuery.filter(rangeQuery);
        }
        searchSourceBuilder.query(boolQuery);
        // ======================================模糊匹配 + 过滤=========================================

        // ===========================================排序==============================================
        // saleCount_asc
        if (!StringUtils.isEmpty(searchParam.getSort())) {
            String[] sort = searchParam.getSort().split("_");
            if (sort.length >= 2) {
                SortOrder order = sort[1].equalsIgnoreCase("asc")? SortOrder.ASC : SortOrder.DESC;
                searchSourceBuilder.sort(sort[0], order);
            }
        }
        // ===========================================排序==============================================

        // ===========================================page==============================================
        searchSourceBuilder.from((searchParam.getPageNum() - 1) * ESConstant.SEARCH_PAGE_SIZE);
        searchSourceBuilder.size(ESConstant.SEARCH_PAGE_SIZE);
        // ===========================================page==============================================

        // ===========================================高亮==============================================
        if (!StringUtils.isEmpty(searchParam.getKeyword())) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuTitle");
            highlightBuilder.preTags("<b style='color:red'>");
            highlightBuilder.postTags("</b>");
            searchSourceBuilder.highlighter(highlightBuilder);
        }
        // ===========================================高亮==============================================

        // ==========================================聚合分析===========================================
        // BrandAgg
        TermsAggregationBuilder brandAgg = AggregationBuilders.terms("brand_agg");
        brandAgg.field("brandId").size(50);
        TermsAggregationBuilder brandNameAgg = AggregationBuilders.terms("brand_name_agg");
        brandNameAgg.field("brandName").size(1);
        TermsAggregationBuilder brandImgAgg = AggregationBuilders.terms("brand_img_agg");
        brandImgAgg.field("brandImg").size(1);
        brandAgg.subAggregation(brandNameAgg);
        brandAgg.subAggregation(brandImgAgg);
        searchSourceBuilder.aggregation(brandAgg);

        // CatalogAgg
        TermsAggregationBuilder catalogAgg = AggregationBuilders.terms("catalog_agg");
        catalogAgg.field("catalogId").size(20);
        TermsAggregationBuilder catalogNameAgg = AggregationBuilders.terms("catalog_name_agg");
        catalogNameAgg.field("catalogName").size(1);
        catalogAgg.subAggregation(catalogNameAgg);
        searchSourceBuilder.aggregation(catalogAgg);

        // AttrsAgg
        NestedAggregationBuilder attrAgg = AggregationBuilders.nested("attr_agg", "attrs");
        TermsAggregationBuilder attrIdAgg = AggregationBuilders.terms("attr_id_agg");
        attrIdAgg.field("attrs.attrId");
        TermsAggregationBuilder attrNameAgg = AggregationBuilders.terms("attr_name_agg");
        attrNameAgg.field("attrs.attrName").size(1);
        TermsAggregationBuilder attrValueAgg = AggregationBuilders.terms("attr_value_agg");
        attrValueAgg.field("attrs.attrValue").size(50);
        attrIdAgg.subAggregation(attrNameAgg);
        attrIdAgg.subAggregation(attrValueAgg);
        attrAgg.subAggregation(attrIdAgg);
        searchSourceBuilder.aggregation(attrAgg);
        // ==========================================聚合分析===========================================

        searchRequest.source(searchSourceBuilder);
        return searchRequest;
    }

    /**
     * Build Search Result
     * @param search search Response
     * @param param Request Param
     * @return result
     */
    private SearchResult buildResult(SearchResponse search, SearchParam param) {
        ObjectMapper mapper = new ObjectMapper();
        SearchResult searchResult = new SearchResult();

        SearchHits hits = search.getHits();
        // 1. Product Info
        List<SkuESModel> products = new ArrayList<>();
        for (SearchHit hit : hits.getHits()) {
            SkuESModel skuESModel = new SkuESModel();
            try {
                skuESModel = mapper.readValue(hit.getSourceAsString(), SkuESModel.class);
                // If has keyword, highlight it.
                if (!StringUtils.isEmpty(param.getKeyword())) {
                    HighlightField skuTitle = hit.getHighlightFields().get("skuTitle");
                    String content = skuTitle.getFragments()[0].string();
                    skuESModel.setSkuTitle(content);
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            products.add(skuESModel);
        }
        searchResult.setProducts(products);

        // 2. Paging Info
        // 2.1 Total Hits Records
        long total = hits.getTotalHits().value;
        searchResult.setTotal(total);
        // 2.2 Total Page
        long totalPage = total / ESConstant.SEARCH_PAGE_SIZE +
                total % ESConstant.SEARCH_PAGE_SIZE == 0 ? 0 : 1;
        searchResult.setTotalPages((int) totalPage);
        // 2.3 Current Page
        searchResult.setPageNum(param.getPageNum());

        Aggregations aggregations = search.getAggregations();
        // 3. Brands Info
        List<SearchResult.BrandVo> brands = new ArrayList<>();
        ParsedLongTerms brandAgg = aggregations.get("brand_agg");
        for (Terms.Bucket bucket : brandAgg.getBuckets()) {
            Long brandId = (Long) bucket.getKey();
            ParsedStringTerms brandNameAgg = bucket.getAggregations().get("brand_name_agg");
            String brandName = brandNameAgg.getBuckets().get(0).getKeyAsString();
            ParsedStringTerms brandImgAgg = bucket.getAggregations().get("brand_img_agg");
            String brandImg = brandImgAgg.getBuckets().get(0).getKeyAsString();
            var brand = new SearchResult.BrandVo();
            brand.setBrandId(brandId);
            brand.setBrandImg(brandImg);
            brand.setBrandName(brandName);
            brands.add(brand);
        }
        searchResult.setBrands(brands);

        // Category Info
        List<SearchResult.CatalogVo> catalogs = new ArrayList<>();
        ParsedLongTerms catalogAgg = aggregations.get("catalog_agg");
        for (Terms.Bucket bucket : catalogAgg.getBuckets()) {
            Long catalogId = (Long) bucket.getKey();
            ParsedStringTerms catalogNameAgg = bucket.getAggregations().get("catalog_name_agg");
            String catalogName = catalogNameAgg.getBuckets().get(0).getKeyAsString();
            var catalog = new SearchResult.CatalogVo();
            catalog.setCatalogId(catalogId);
            catalog.setCatalogName(catalogName);
            catalogs.add(catalog);
        }
        searchResult.setCatalogs(catalogs);

        // Attrs Info
        List<SearchResult.AttrVo> attrs = new ArrayList<>();
        ParsedNested attrAgg = aggregations.get("attr_agg");
        ParsedLongTerms attrIdAgg = attrAgg.getAggregations().get("attr_id_agg");
        for (Terms.Bucket bucket : attrIdAgg.getBuckets()) {
            Long attrId = (Long) bucket.getKey();
            ParsedStringTerms attrNameAgg = bucket.getAggregations().get("attr_name_agg");
            String attrName = attrNameAgg.getBuckets().get(0).getKeyAsString();
            ParsedStringTerms attrValueAgg = bucket.getAggregations().get("attr_value_agg");
            List<String> attrValue = attrValueAgg.getBuckets().stream()
                    .map(MultiBucketsAggregation.Bucket::getKeyAsString)
                    .collect(Collectors.toList());
            SearchResult.AttrVo attr = new SearchResult.AttrVo();
            attr.setAttrId(attrId);
            attr.setAttrName(attrName);
            attr.setAttrValue(attrValue);
            attrs.add(attr);
        }
        searchResult.setAttrs(attrs);


        return searchResult;
    }
}
