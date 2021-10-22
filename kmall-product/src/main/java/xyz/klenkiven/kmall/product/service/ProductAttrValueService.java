package xyz.klenkiven.kmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.klenkiven.kmall.common.utils.PageUtils;
import xyz.klenkiven.kmall.product.entity.ProductAttrValueEntity;
import xyz.klenkiven.kmall.product.vo.BaseAttrs;

import java.util.List;
import java.util.Map;

/**
 * spu属性值
 *
 * @author klenkiven
 * @email wzl709@outlook.com
 * @date 2021-08-29 16:07:27
 */
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * Save Base Attribute
     */
    void saveBaseAttrs(Long id, List<BaseAttrs> baseAttrs);

    /**
     * 获取spu规格
     */
    List<ProductAttrValueEntity> listBaseAttrForSpu(Long spuId);

    /**
     * Update SPU Attribute
     */
    void updateSpuAttr(Long spuId, List<ProductAttrValueEntity> entities);
}

