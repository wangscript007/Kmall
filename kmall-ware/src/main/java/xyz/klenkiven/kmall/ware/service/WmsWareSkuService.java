package xyz.klenkiven.kmall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.klenkiven.common.utils.PageUtils;
import xyz.klenkiven.kmall.ware.entity.WmsWareSkuEntity;

import java.util.Map;

/**
 * 商品库存
 *
 * @author klenkiven
 * @email wzl709@outlook.com
 * @date 2021-08-29 21:12:37
 */
public interface WmsWareSkuService extends IService<WmsWareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

