package xyz.klenkiven.kmall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.klenkiven.kmall.common.utils.PageUtils;
import xyz.klenkiven.kmall.order.entity.OrderSettingEntity;

import java.util.Map;

/**
 * 订单配置信息
 *
 * @author klenkiven
 * @email wzl709@outlook.com
 * @date 2021-08-29 21:06:10
 */
public interface OrderSettingService extends IService<OrderSettingEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

