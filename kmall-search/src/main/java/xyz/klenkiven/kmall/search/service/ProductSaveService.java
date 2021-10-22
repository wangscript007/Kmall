package xyz.klenkiven.kmall.search.service;


import xyz.klenkiven.kmall.common.to.elasticsearch.SkuESModel;

import java.io.IOException;
import java.util.List;

/**
 * ElasticSearch - Product - Save Action
 * @author klenkiven
 */
public interface ProductSaveService {

    /**
     * Save product to up
     */
    boolean saveProductUp(List<SkuESModel> skuESModelList) throws IOException;

}
