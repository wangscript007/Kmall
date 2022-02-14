package xyz.klenkiven.kmall.cart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import xyz.klenkiven.kmall.cart.interceptor.CartInterceptor;
import xyz.klenkiven.kmall.cart.vo.UserInfoVO;

/**
 * Cart Web Controller
 * @author klenkiven
 */
@Controller
public class CartController {

    @GetMapping("/cart.html")
    public String cartListPage() {
        UserInfoVO userInfoVO = CartInterceptor.threadLocal.get();
        System.out.println(userInfoVO);

        return "cartList";
    }

}
