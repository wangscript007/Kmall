package xyz.klenkiven.kmall.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import xyz.klenkiven.kmall.cart.feign.ProductFeignService;
import xyz.klenkiven.kmall.cart.interceptor.CartInterceptor;
import xyz.klenkiven.kmall.cart.service.CartService;
import xyz.klenkiven.kmall.cart.vo.CartItemVO;
import xyz.klenkiven.kmall.cart.vo.CartVO;
import xyz.klenkiven.kmall.cart.vo.UserInfoVO;
import xyz.klenkiven.kmall.common.to.SkuInfoTO;
import xyz.klenkiven.kmall.common.utils.R;
import xyz.klenkiven.kmall.common.utils.Result;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * Cart Service Implements
 * @author klenkiven
 */
// @Slf4j // private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LogExample.class);
@Service
public class CartServiceImpl implements CartService {

    private static final Logger log = LoggerFactory.getLogger(CartService.class);
    public static final String CART_PREFIX = "kmall:cart:";
    public static final String INVALID_TEMP_CART = "-1";

    private final StringRedisTemplate redisTemplate;
    private final ProductFeignService productFeignService;
    private final ThreadPoolExecutor executor;

    @Override
    public void addToCart(Integer num, Long skuId) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();

        String json = (String) cartOps.get(skuId.toString());
        CartItemVO cartItem;
        // if this item is not in cart, add it, else update it
        if (StringUtils.isEmpty(json)) {
            cartItem = addNewItemToCart(num, skuId);
        } else {
            cartItem = JSON.parseObject(json, CartItemVO.class);
            cartItem.setCount(cartItem.getCount() + num);
        }

        // Save Cart Item
        cartOps.put(cartItem.getSkuId().toString(), JSON.toJSONString(cartItem));
    }

    @Override
    public void deleteCart(String cartId) {
        redisTemplate.delete(CART_PREFIX + cartId);
    }

    @Override
    public CartItemVO getCartItem(Long skuId) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        String s = (String) cartOps.get(skuId.toString());
        return JSON.parseObject(s, CartItemVO.class);
    }

    @Override
    public CartVO getCart() {
        UserInfoVO userInfoVO = CartInterceptor.threadLocal.get();
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        CartVO cart = new CartVO();

        // Merge Temp Cart and delete temp cart, IF temp cart EXIST
        if (userInfoVO.getUserId() != null) {
            if (!INVALID_TEMP_CART.equals(userInfoVO.getUserKey())) {

                String cartKey = CART_PREFIX + userInfoVO.getUserKey();
                BoundHashOperations<String, Object, Object> tempCartOps = redisTemplate.boundHashOps(cartKey);

                List<Object> tempCartItemList = tempCartOps.values();
                List<CartItemVO> tempItemList = getCartItemList(tempCartItemList);
                if (tempItemList != null) {
                    // Merge Cart
                    for (CartItemVO item : tempItemList) {
                        addToCart(item.getCount(), item.getSkuId());
                    }
                    // Delete Temp Cart
                    deleteCart(userInfoVO.getUserKey());
                    userInfoVO.setUserKey(INVALID_TEMP_CART);
                }
            }
        }

        // Get Cart Item
        List<Object> values = cartOps.values();
        List<CartItemVO> itemList = getCartItemList(values);
        cart.setItems(itemList);

        return cart;
    }

    @Override
    public void checkItem(Long skuId, Integer isChecked) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        CartItemVO cartItem = getCartItem(skuId);
        cartItem.setCheck(isChecked == 1);
        cartOps.put(skuId.toString(), JSON.toJSONString(cartItem));
    }

    @Override
    public void countItem(Long skuId, Integer count) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        CartItemVO cartItem = getCartItem(skuId);
        cartItem.setCount(count);
        cartOps.put(skuId.toString(), JSON.toJSONString(cartItem));
    }

    @Override
    public void deleteItem(Long skuId) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        cartOps.delete(skuId.toString());
    }

    /**
     * If Item is not in this cart, get SKU info
     * @param num item count
     * @param skuId item SKU id
     * @return Cart item Object
     */
    private CartItemVO addNewItemToCart(Integer num, Long skuId) {
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

        // Completed Future and return
        CompletableFuture.allOf(skuInfoFuture, attrFuture).join();
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

    /**
     * Get Item List from Cart Object List
     * @param values values
     * @return cart item
     */
    private List<CartItemVO> getCartItemList(List<Object> values) {
        List<CartItemVO> itemList = null;
        if (values != null && values.size() > 0) {
            itemList = values.stream().map((item) -> {
                String s = (String) item;
                return JSON.parseObject(s, CartItemVO.class);
            }).collect(Collectors.toList());
        }
        return itemList;
    }

    public CartServiceImpl(StringRedisTemplate redisTemplate,
                           ProductFeignService productFeignService,
                           ThreadPoolExecutor executor) {
        this.redisTemplate = redisTemplate;
        this.productFeignService = productFeignService;
        this.executor = executor;
    }
}
