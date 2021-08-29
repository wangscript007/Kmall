package xyz.klenkiven.kmall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.klenkiven.common.utils.PageUtils;
import xyz.klenkiven.kmall.order.entity.MqMessageEntity;

import java.util.Map;

/**
 * 
 *
 * @author klenkiven
 * @email wzl709@outlook.com
 * @date 2021-08-29 21:06:10
 */
public interface MqMessageService extends IService<MqMessageEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

