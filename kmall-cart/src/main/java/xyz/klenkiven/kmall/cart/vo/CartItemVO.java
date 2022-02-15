package xyz.klenkiven.kmall.cart.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Cart Item Content
 * @author klenkiven
 */
public class CartItemVO {

    private Long skuId;             // skuId
    private Boolean check = true;   // 是否选中
    private String title;           // 标题
    private String image;           // 图片
    private List<String> skuAttrValues;// 商品销售属性
    private BigDecimal price;       // 单价
    private Integer count;          // 当前商品数量
    private BigDecimal totalPrice;  // 总价

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getSkuAttrValues() {
        return skuAttrValues;
    }

    public void setSkuAttrValues(List<String> skuAttrValues) {
        this.skuAttrValues = skuAttrValues;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public BigDecimal getTotalPrice() {
        return new BigDecimal(String.valueOf(price))
                .multiply(BigDecimal.valueOf(count));
    }

}
