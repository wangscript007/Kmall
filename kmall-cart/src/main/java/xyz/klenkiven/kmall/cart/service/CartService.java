package xyz.klenkiven.kmall.cart.service;

import xyz.klenkiven.kmall.cart.vo.CartItemVO;
import xyz.klenkiven.kmall.cart.vo.CartVO;

/**
 * Cart Service - Do Cart Related Service
 * @author klenkiven
 */
public interface CartService {

    /**
     * Save to Temp User's Cart
     * @param num item's quantity
     * @param skuId item's id
     */
    void addToCart(Integer num, Long skuId);

    /**
     * Delete Cart
     * <p>mostly use this cart to remove temp cart (user-key cart)</p>
     * @param cartId cart id can be user id or user-key
     */
    void deleteCart(String cartId);

    /**
     * Get Cart Single Item
     * @param skuId item's SKU id
     * @return item
     */
    CartItemVO getCartItem(Long skuId);

    /**
     * Get Cart Info
     * @return Cart
     */
    CartVO getCart();

}
