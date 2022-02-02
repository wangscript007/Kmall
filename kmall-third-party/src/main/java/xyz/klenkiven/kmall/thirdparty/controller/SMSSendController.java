package xyz.klenkiven.kmall.thirdparty.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.klenkiven.kmall.common.utils.Result;
import xyz.klenkiven.kmall.thirdparty.component.SMSComponent;

/**
 * SMS Code Controller For Send
 * @author klenkiven
 */
@RestController
@RequestMapping("/third-party/sms")
@RequiredArgsConstructor
public class SMSSendController {

    private final SMSComponent sms;

    /**
     * Send Code
     */
    @GetMapping("/sendCode")
    public Result<?> sendCode(@RequestParam("phone") String phone,
                              @RequestParam("code") String code) {
        sms.sendSMSCode(phone, code);
        return Result.ok();
    }

}
