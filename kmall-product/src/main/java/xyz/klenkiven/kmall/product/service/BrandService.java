package xyz.klenkiven.kmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.klenkiven.kmall.common.utils.PageUtils;
import xyz.klenkiven.kmall.product.entity.BrandEntity;

import java.util.Map;

/**
 * 品牌
 *
 * @author klenkiven
 * @email wzl709@outlook.com
 * @date 2021-08-29 16:07:27
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

