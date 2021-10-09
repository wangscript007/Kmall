package xyz.klenkiven.kmall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.klenkiven.kmall.common.utils.PageUtils;
import xyz.klenkiven.kmall.order.entity.OrderEntity;

import java.util.Map;

/**
 * 订单
 *
 * @author klenkiven
 * @email wzl709@outlook.com
 * @date 2021-08-29 21:06:10
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

