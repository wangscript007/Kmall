package xyz.klenkiven.kmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.klenkiven.kmall.common.utils.PageUtils;
import xyz.klenkiven.kmall.product.entity.SkuSaleAttrValueEntity;
import xyz.klenkiven.kmall.product.vo.SkuItemVO;

import java.util.List;
import java.util.Map;

/**
 * sku销售属性&值
 *
 * @author klenkiven
 * @email wzl709@outlook.com
 * @date 2021-08-29 16:07:27
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<SkuItemVO.SkuItemSaleAttrVO> getSaleAttr(Long spuId);

    /**
     * Get SKU Sale Attribute as List
     * @param skuId SKU id
     * @return SKU attribute in String list
     */
    List<String> getSkuSaleAttrStringList(Long skuId);
}

