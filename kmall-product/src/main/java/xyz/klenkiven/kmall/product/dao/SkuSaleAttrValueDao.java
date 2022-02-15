package xyz.klenkiven.kmall.product.dao;

import xyz.klenkiven.kmall.product.entity.SkuSaleAttrValueEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.klenkiven.kmall.product.vo.SkuItemVO;

import java.util.List;

/**
 * sku销售属性&值
 * 
 * @author klenkiven
 * @email wzl709@outlook.com
 * @date 2021-08-29 16:07:27
 */
@Mapper
public interface SkuSaleAttrValueDao extends BaseMapper<SkuSaleAttrValueEntity> {

    List<SkuItemVO.SkuItemSaleAttrVO> getSaleAttr(Long spuId);

    /**
     * Get SKU Sale Attribute as List
     * @param skuId SKU ID
     * @return string list
     */
    List<String> getSkuSaleAttrStringList(Long skuId);
}
