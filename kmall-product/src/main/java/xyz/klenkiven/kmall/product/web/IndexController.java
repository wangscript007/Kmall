package xyz.klenkiven.kmall.product.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.klenkiven.kmall.product.entity.CategoryEntity;
import xyz.klenkiven.kmall.product.service.CategoryService;
import xyz.klenkiven.kmall.product.vo.Catalog2VO;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Index Page Controller
 * @author klenkiven
 */
@Controller
@Slf4j
@RequiredArgsConstructor
public class IndexController {

    private final CategoryService categoryService;
    private final RedissonClient redissonClient;

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

        return categoryService.getCatalogJsonFromDbWithDistributeLock();
    }

    @GetMapping("product/hello")
    @ResponseBody
    public String hello() {
        // 1. Get Distribute Lock
        RLock lock = redissonClient.getLock("klenkiven-redisson-lock");
        // 2. Lock
        lock.lock();
        // lock.lock(10, TimeUnit.SECONDS);
        try {
            System.out.println("Do Business by Thread-" + Thread.currentThread().getId());
            Thread.sleep(30 * 1000);
        } catch (Exception e) {
            System.out.println("Business Exception by Thread-" + Thread.currentThread().getId());
        } finally {
            System.out.println("Release Lock by Thread-" + Thread.currentThread().getId());
            lock.unlock();
        }


        return "hello";
    }

}
