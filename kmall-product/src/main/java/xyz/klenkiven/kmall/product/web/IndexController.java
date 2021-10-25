package xyz.klenkiven.kmall.product.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.klenkiven.kmall.product.entity.CategoryEntity;
import xyz.klenkiven.kmall.product.service.CategoryService;

import java.util.List;

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

}
