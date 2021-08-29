package xyz.klenkiven.kmall.coupon.dao;

import xyz.klenkiven.kmall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author klenkiven
 * @email wzl709@outlook.com
 * @date 2021-08-29 20:46:13
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
