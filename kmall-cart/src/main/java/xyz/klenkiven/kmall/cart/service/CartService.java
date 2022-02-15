package xyz.klenkiven.kmall.cart.service;

import xyz.klenkiven.kmall.cart.vo.CartItemVO;

/**
 * Cart Service - Do Cart Related Service
 * @author klenkiven
 */
public interface CartService {

    /**
     * Save to Temp User's Cart
     * @param num item's quantity
     * @param skuId item's id
     * @return CartItem
     */
    CartItemVO addToCart(Integer num, Long skuId);
}
