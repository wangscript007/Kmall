package xyz.klenkiven.kmall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.klenkiven.kmall.common.utils.PageUtils;
import xyz.klenkiven.kmall.member.entity.IntegrationChangeHistoryEntity;

import java.util.Map;

/**
 * 积分变化历史记录
 *
 * @author klenkiven
 * @email wzl709@outlook.com
 * @date 2021-08-29 20:56:00
 */
public interface IntegrationChangeHistoryService extends IService<IntegrationChangeHistoryEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

