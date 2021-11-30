package xyz.klenkiven.kmall.search.service;

import xyz.klenkiven.kmall.search.vo.SearchParam;
import xyz.klenkiven.kmall.search.vo.SearchResult;

/**
 * Mall Search Service
 * @author klenkiven
 */
public interface MallSearchService {

    /**
     * Search Data From Elasticsearch
     * @param searchParam search parameter
     * @return result
     */
    SearchResult search(SearchParam searchParam);

}
