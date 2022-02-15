package xyz.klenkiven.kmall.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import xyz.klenkiven.kmall.cart.feign.ProductFeignService;
import xyz.klenkiven.kmall.cart.interceptor.CartInterceptor;
import xyz.klenkiven.kmall.cart.service.CartService;
import xyz.klenkiven.kmall.cart.vo.CartItemVO;
import xyz.klenkiven.kmall.cart.vo.UserInfoVO;
import xyz.klenkiven.kmall.common.to.SkuInfoTO;
import xyz.klenkiven.kmall.common.utils.R;
import xyz.klenkiven.kmall.common.utils.Result;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Cart Service Implements
 * @author klenkiven
 */
// @Slf4j // private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LogExample.class);
@Service
public class CartServiceImpl implements CartService {

    private static final Logger log = LoggerFactory.getLogger(CartService.class);
    public static final String CART_PREFIX = "kmall:cart:";

    private final StringRedisTemplate redisTemplate;
    private final ProductFeignService productFeignService;
    private final ThreadPoolExecutor executor;

    @Override
    public CartItemVO addToCart(Integer num, Long skuId) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        // Add to cart
        CartItemVO cartItem = new CartItemVO();
        cartItem.setCheck(true);
        cartItem.setCount(num);
        cartItem.setSkuId(skuId);

        // [Remote] Get Sku Info
        CompletableFuture<Void> skuInfoFuture = CompletableFuture.runAsync(() -> {
            R skuInfo = productFeignService.getSkuInfo(skuId);
            SkuInfoTO skuInfoTO = skuInfo.getData("skuInfo", new TypeReference<>() {
            });
            cartItem.setPrice(skuInfoTO.getPrice());
            cartItem.setImage(skuInfoTO.getSkuDefaultImg());
            cartItem.setTitle(skuInfoTO.getSkuTitle());
        }, executor);

        // [Remote] Get SKU's Attr
        CompletableFuture<Void> attrFuture = CompletableFuture.runAsync(() -> {
            Result<List<String>> listResult = productFeignService.skuSaleAttrStringList(skuId);
            cartItem.setSkuAttrValues(listResult.getData());
        }, executor);

        // Completed Future and Save Cart Item
        CompletableFuture.allOf(skuInfoFuture, attrFuture).join();
        cartOps.put(cartItem.getSkuId().toString(), JSON.toJSONString(cartItem));
        return cartItem;
    }

    /**
     * Get Cart Operations
     * @return hash ops
     */
    private BoundHashOperations<String, Object, Object> getCartOps() {
        UserInfoVO userInfoVO = CartInterceptor.threadLocal.get();
        String cartKey;
        if (userInfoVO.getUserId() != null) {
            cartKey = CART_PREFIX + userInfoVO.getUserId();
        } else {
            cartKey = CART_PREFIX + userInfoVO.getUserKey();
        }
        return redisTemplate.boundHashOps(cartKey);
    }

    public CartServiceImpl(StringRedisTemplate redisTemplate,
                           ProductFeignService productFeignService,
                           ThreadPoolExecutor executor) {
        this.redisTemplate = redisTemplate;
        this.productFeignService = productFeignService;
        this.executor = executor;
    }
}
