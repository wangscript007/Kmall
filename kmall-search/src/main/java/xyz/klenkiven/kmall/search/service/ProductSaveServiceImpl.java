package xyz.klenkiven.kmall.search.service;

import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.klenkiven.kmall.common.to.elasticsearch.SkuESModel;
import xyz.klenkiven.kmall.search.ESConstant;
import xyz.klenkiven.kmall.search.config.KmallElasticSearchConfig;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductSaveServiceImpl implements ProductSaveService {

    private final RestHighLevelClient esClient;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean saveProductUp(List<SkuESModel> skuESModelList) throws IOException {
        // Save Index Batch: index -> id -> source
        BulkRequest bulkRequest = new BulkRequest();
        skuESModelList.forEach(esModel -> {
            IndexRequest indexRequest = new IndexRequest(ESConstant.PRODUCT_INDEX);
            indexRequest.id(esModel.getSkuId().toString());
            indexRequest.source(JSON.toJSONString(esModel), XContentType.JSON);
            bulkRequest.add(indexRequest);
        });
        BulkResponse bulk = esClient.bulk(bulkRequest, KmallElasticSearchConfig.COMMON_OPTIONS);

        // If Failures do that
        boolean b = bulk.hasFailures();
        List<String> collect = Arrays.stream(bulk.getItems())
                .map(BulkItemResponse::getId)
                .collect(Collectors.toList());
        if (b) {
            log.error("Product Status Up Error: {}", collect);
        } else {
            log.info("Product Status Up Success: {}", collect);
        }
        return !b;
    }

}
