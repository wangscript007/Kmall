package xyz.klenkiven.kmall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.klenkiven.common.utils.PageUtils;
import xyz.klenkiven.kmall.ware.entity.WmsPurchaseDetailEntity;

import java.util.Map;

/**
 * 
 *
 * @author klenkiven
 * @email wzl709@outlook.com
 * @date 2021-08-29 21:12:37
 */
public interface WmsPurchaseDetailService extends IService<WmsPurchaseDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

