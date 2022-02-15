package xyz.klenkiven.kmall.product.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import xyz.klenkiven.kmall.product.service.SkuInfoService;
import xyz.klenkiven.kmall.product.vo.SkuItemVO;

/**
 * Item Controller
 * @author klenkiven
 */
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final SkuInfoService skuInfoService;

    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable Long skuId, Model model) {
        // System.out.println("Sku Id is " + skuId);
        SkuItemVO vo =  skuInfoService.item(skuId);
        model.addAttribute("item", vo);
        System.out.println("SKU Info: " + vo.getInfo());
        return "item";
    }

}
