package xyz.klenkiven.auth.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import xyz.klenkiven.auth.vo.RegForm;
import xyz.klenkiven.auth.vo.UserLoginForm;
import xyz.klenkiven.kmall.common.utils.Result;

/**
 * Member Remote HTTP Client
 * @author klenkiven
 */
@FeignClient("kmall-member")
public interface MemberFeignService {
    /**
     * [FEIGN] ] Register Service
     */
    @PostMapping("/member/member/register")
    public Result<?> register(@RequestBody RegForm vo);

    /**
     * [FEIGN] Member Login
     */
    @PostMapping("/member/member/login")
    public Result<?> login(@RequestBody UserLoginForm form);
}
