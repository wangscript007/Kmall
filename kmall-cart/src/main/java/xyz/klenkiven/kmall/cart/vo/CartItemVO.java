package xyz.klenkiven.kmall.cart.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Cart Item Content
 * @author klenkiven
 */
@Data
public class CartItemVO {

    private Long skuId;             // skuId
    private Boolean check = true;   // 是否选中
    private String title;           // 标题
    private String image;           // 图片
    private List<String> skuAttrValues;// 商品销售属性
    private BigDecimal price;       // 单价
    private Integer count;          // 当前商品数量
    private BigDecimal totalPrice;  // 总价

}
