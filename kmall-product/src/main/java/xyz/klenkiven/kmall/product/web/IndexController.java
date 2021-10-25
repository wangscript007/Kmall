package xyz.klenkiven.kmall.product.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.klenkiven.kmall.product.entity.CategoryEntity;
import xyz.klenkiven.kmall.product.service.CategoryService;
import xyz.klenkiven.kmall.product.vo.Catalog2VO;

import java.util.List;
import java.util.Map;

/**
 * Index Page Controller
 * @author klenkiven
 */
@Controller
@Slf4j
@RequiredArgsConstructor
public class IndexController {

    private final CategoryService categoryService;

    @RequestMapping({"/", "/index.html"})
    public String indexPage(Model model) {
        List<CategoryEntity> categoryList = categoryService.listCategoryByLevel(1);
        log.info("{}", categoryList);
        model.addAttribute("category", categoryList);
        return "index";
    }

    @GetMapping("/index/catalog.json")
    @ResponseBody
    public Map<Long, List<Catalog2VO>> getCatalogJson() {

        return categoryService.getCatalogJson();
    }

}
