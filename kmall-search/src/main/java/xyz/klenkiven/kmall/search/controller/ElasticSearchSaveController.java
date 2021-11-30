package xyz.klenkiven.kmall.search.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.klenkiven.kmall.common.exception.ExceptionCodeEnum;
import xyz.klenkiven.kmall.common.to.elasticsearch.SkuESModel;
import xyz.klenkiven.kmall.common.utils.Result;
import xyz.klenkiven.kmall.search.service.ProductSaveService;

import java.io.IOException;
import java.util.List;

/**
 * ElasticSearch Save Action Controller
 * @author klenkiven
 * @email wzl709@outlook.com
 */
@Slf4j
@RequestMapping("/search/save")
@RestController
@RequiredArgsConstructor
public class ElasticSearchSaveController {

    private final ProductSaveService productSaveService;

    /**
     * Save Product Up
     */
    @PostMapping("/product")
    public Result<?> productStatusUp(@RequestBody List<SkuESModel> skuESModelList) {
        boolean result;
        try {
            result = productSaveService.saveProductUp(skuESModelList);
        } catch (IOException e) {
            log.error("[ERROR] ElasticSearch Product Up Error: {}", e.getMessage());
            return Result.error(ExceptionCodeEnum.PRODUCT_UP_ERROR);
        }

        if (result) {
            return Result.ok();
        } else {
            return Result.error(ExceptionCodeEnum.PRODUCT_UP_ERROR);
        }
    }

}
