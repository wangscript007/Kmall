package xyz.klenkiven.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import xyz.klenkiven.auth.feign.MemberFeignService;
import xyz.klenkiven.auth.model.QQAccessToken;
import xyz.klenkiven.kmall.common.exception.ExceptionCodeEnum;
import xyz.klenkiven.kmall.common.utils.Result;

/**
 * OAuth 2.0 Controller
 * @author klenkiven
 */
@Controller
@RequestMapping("/oauth2.0")
@RequiredArgsConstructor
public class OAuth2Controller {

    private final MemberFeignService memberFeignService;

    @GetMapping("/qq/success")
    public String qqSuccess(String code) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        // headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://graph.qq.com/oauth2.0/token")
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", "101991731")
                .queryParam("client_secret", "731b90fee856caed159b3a8d4f05779e")
                .queryParam("code", code)
                .queryParam("redirect_uri", "http://auth.kmall.com/oauth2.0/qq/success");
        System.out.println("QQ Auth URL (server-side): " + builder.toUriString());
        ResponseEntity<QQAccessToken> exchange = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                QQAccessToken.class);
        if (exchange.getStatusCode() != HttpStatus.OK) {
            return "redirect:http://auth.kmall.com/login.html";
        }

        // Do Login
        QQAccessToken body = exchange.getBody();
        Result<?> result = memberFeignService.qqOauthLogin(body);
        if (result.getCode() != 0) {
            return "redirect:http://auth.kmall.com/login.html";
        }

        // Login success
        return "redirect:http://kmall.com/";
    }

}
