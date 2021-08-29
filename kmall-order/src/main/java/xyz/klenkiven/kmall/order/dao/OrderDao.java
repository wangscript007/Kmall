package xyz.klenkiven.kmall.order.dao;

import xyz.klenkiven.kmall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author klenkiven
 * @email wzl709@outlook.com
 * @date 2021-08-29 21:06:10
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
