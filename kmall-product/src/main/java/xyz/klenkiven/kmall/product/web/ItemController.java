package xyz.klenkiven.kmall.product.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Item Controller
 * @author klenkiven
 */
@Controller
public class ItemController {

    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable String skuId) {
        System.out.println(skuId);
        return "item";
    }

}
