package xyz.klenkiven.auth.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import xyz.klenkiven.auth.feign.ThirdPartyFeignService;
import xyz.klenkiven.auth.vo.RegForm;
import xyz.klenkiven.kmall.common.constant.SMSConstant;
import xyz.klenkiven.kmall.common.utils.Result;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Login and Register Controller
 * @author klenkiven
 */
@Controller
@RequiredArgsConstructor
public class LoginRegController {

    private final ThirdPartyFeignService thirdParty;
    private final StringRedisTemplate redisTemplate;

    @GetMapping("/sms/sendCode")
    @ResponseBody
    public Result<?> sendCode(@RequestParam("phone") String phone) {
        // 1. Prevent SMS Frequently Send
        // 2. Guarantee SMS Send after 60 seconds
        String redisCode = redisTemplate.opsForValue().get(SMSConstant.SMS_CODE_PREFIX + phone);
        if (!StringUtils.isEmpty(redisCode)){
            long time = System.currentTimeMillis() - Long.parseLong(redisCode.split("_")[1]);
            if (time < 60 * 1000) {
                return Result.error("Send Too Frequently");
            }
        }
        // Do Send Message
        int randomCode = UUID.randomUUID().hashCode() % 1000000;
        thirdParty.sendCode(phone, String.valueOf(randomCode));
        redisTemplate.opsForValue().set(SMSConstant.SMS_CODE_PREFIX + phone,
                randomCode + "_" + System.currentTimeMillis(),
                5, TimeUnit.MINUTES);
        return Result.ok();
    }

    @PostMapping("/register")
    public String register(@Valid RegForm from, BindingResult result,
                           RedirectAttributes attributes) {
        // If form has validation errors, redirect to register page
        if (result.hasErrors()) {
            System.out.println(result);
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(fieldError -> {
                if (!errors.containsKey(fieldError.getField())) {
                    errors.put(fieldError.getField(), fieldError.getDefaultMessage());
                }
            });
            // Use Session store data, then redirect to other page, finally get the data from session
            // There will cause many problem, especially in TODO distributed session situation
            attributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.kmall.com/reg.html";
        }
        // Do Register with Other Service


        // Register success
        return "redirect:http://auth.kmall.com/login.html";
    }

}
