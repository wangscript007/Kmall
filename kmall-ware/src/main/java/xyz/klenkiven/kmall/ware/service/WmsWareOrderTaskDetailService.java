package xyz.klenkiven.kmall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.klenkiven.common.utils.PageUtils;
import xyz.klenkiven.kmall.ware.entity.WmsWareOrderTaskDetailEntity;

import java.util.Map;

/**
 * 库存工作单
 *
 * @author klenkiven
 * @email wzl709@outlook.com
 * @date 2021-08-29 21:12:37
 */
public interface WmsWareOrderTaskDetailService extends IService<WmsWareOrderTaskDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

