package xyz.klenkiven.kmall.cart.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import xyz.klenkiven.kmall.cart.interceptor.CartInterceptor;
import xyz.klenkiven.kmall.cart.service.CartService;
import xyz.klenkiven.kmall.cart.vo.CartItemVO;
import xyz.klenkiven.kmall.cart.vo.UserInfoVO;

/**
 * Cart Web Controller
 * @author klenkiven
 */
@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/cart.html")
    public String cartListPage() {
        UserInfoVO userInfoVO = CartInterceptor.threadLocal.get();
        System.out.println(userInfoVO);

        return "cartList";
    }

    @GetMapping("/addToCart")
    public String addToCart(Integer num, Long skuId, Model model) {
        CartItemVO item = cartService.addToCart(num, skuId);
        model.addAttribute("item", item);
        return "success";
    }

}
