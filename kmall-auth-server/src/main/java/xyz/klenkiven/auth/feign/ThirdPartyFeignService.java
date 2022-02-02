package xyz.klenkiven.auth.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.klenkiven.kmall.common.utils.Result;

@FeignClient("kmall-third-party")
public interface ThirdPartyFeignService {

    /**
     * Send Code
     */
    @GetMapping("/third-party/sms/sendCode")
    Result<?> sendCode(@RequestParam("phone") String phone,
                       @RequestParam("code") String code);
}
