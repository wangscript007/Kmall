package xyz.klenkiven.kmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.klenkiven.kmall.common.utils.PageUtils;
import xyz.klenkiven.kmall.product.entity.SpuInfoEntity;
import xyz.klenkiven.kmall.product.vo.SpuSaveVO;

import java.util.Map;

/**
 * spu信息
 *
 * @author klenkiven
 * @email wzl709@outlook.com
 * @date 2021-08-29 16:07:27
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void savSpuInfo(SpuSaveVO spuSaveVO);

    /**
     * 商品上架
     */
    void productUp(Long spuId);
}

