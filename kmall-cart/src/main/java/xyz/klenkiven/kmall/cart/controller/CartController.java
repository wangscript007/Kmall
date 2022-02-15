package xyz.klenkiven.kmall.cart.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import xyz.klenkiven.kmall.cart.service.CartService;
import xyz.klenkiven.kmall.cart.vo.CartItemVO;
import xyz.klenkiven.kmall.cart.vo.CartVO;

/**
 * Cart Web Controller
 * @author klenkiven
 */
@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /**
     * Cart Page
     */
    @GetMapping("/cart.html")
    public String cartListPage(Model model) {
        CartVO cart = cartService.getCart();
        model.addAttribute("cart", cart);
        return "cartList";
    }

    /**
     * Change Cart Item Checked
     */
    @GetMapping("/checkItem")
    public String checkItem(@RequestParam Long skuId, @RequestParam Integer check) {
        cartService.checkItem(skuId, check);
        return "redirect:http://cart.kmall.com/cart.html";
    }

    /**
     * Change Cart Item Count
     */
    @GetMapping("/countItem")
    public String countItem(@RequestParam Long skuId, @RequestParam Integer count) {
        cartService.countItem(skuId, count);
        return "redirect:http://cart.kmall.com/cart.html";
    }

    /**
     * Delete Cart Item Action
     */
    @GetMapping("/deleteItem")
    public String deleteItem(@RequestParam Long skuId) {
        cartService.deleteItem(skuId);
        return "redirect:http://cart.kmall.com/cart.html";
    }

    /**
     * Add to Cart Action
     */
    @GetMapping("/addToCart")
    public String addToCart(Integer num, Long skuId, RedirectAttributes attributes) {
        cartService.addToCart(num, skuId);
        attributes.addAttribute("skuId", skuId);
        return "redirect:http://cart.kmall.com/addToCartSuccess.html";
    }

    /**
     * Add to Cart Success Page
     */
    @GetMapping("/addToCartSuccess.html")
    public String addToCarSuccessPage(@RequestParam Long skuId, Model model) {
        CartItemVO cartItem = cartService.getCartItem(skuId);
        model.addAttribute("item", cartItem);
        return "success";
    }

}
