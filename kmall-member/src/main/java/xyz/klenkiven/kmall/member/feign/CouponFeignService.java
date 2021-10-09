package xyz.klenkiven.kmall.member.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.klenkiven.kmall.common.utils.R;

/**
 * 优惠券Feign
 * <p>
 *     <ol>
 *         <li>引入OpenFeign依赖</li>
 *         <li>编写接口，说明接口所需要的远程服务</li>
 *         <li>声明接口说明远程服务的具体请求</li>
 *         <li>开启远程调用服务 {@code @EnableFeignClients}</li>
 *     </ol>
 * </p>
 * @author ：klenkiven
 * @date ：2021/8/30 18:03
 */
@FeignClient("kmall-coupon")
public interface CouponFeignService {

    @RequestMapping("/coupon/coupon/member/list")
    R memberCoupons();

}
