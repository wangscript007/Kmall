package xyz.klenkiven.kmall.cart.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import xyz.klenkiven.kmall.cart.interceptor.CartInterceptor;
import xyz.klenkiven.kmall.cart.service.CartService;
import xyz.klenkiven.kmall.cart.vo.CartItemVO;
import xyz.klenkiven.kmall.cart.vo.CartVO;
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
    public String cartListPage(Model model) {
        CartVO cart = cartService.getCart();
        model.addAttribute("cart", cart);
        return "cartList";
    }

    @GetMapping("/addToCart")
    public String addToCart(Integer num, Long skuId, RedirectAttributes attributes) {
        cartService.addToCart(num, skuId);
        attributes.addAttribute("skuId", skuId);
        return "redirect:http://cart.kmall.com/addToCartSuccess.html";
    }

    @GetMapping("/addToCartSuccess.html")
    public String addToCarSuccessPage(@RequestParam Long skuId, Model model) {
        CartItemVO cartItem = cartService.getCartItem(skuId);
        model.addAttribute("item", cartItem);
        return "success";
    }

}
