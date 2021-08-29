package xyz.klenkiven.kmall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.klenkiven.common.utils.PageUtils;
import xyz.klenkiven.kmall.coupon.entity.CouponEntity;

import java.util.Map;

/**
 * 优惠券信息
 *
 * @author klenkiven
 * @email wzl709@outlook.com
 * @date 2021-08-29 20:46:13
 */
public interface CouponService extends IService<CouponEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

