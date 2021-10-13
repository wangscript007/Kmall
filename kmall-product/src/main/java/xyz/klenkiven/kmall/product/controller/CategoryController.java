package xyz.klenkiven.kmall.product.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import xyz.klenkiven.kmall.product.entity.CategoryEntity;
import xyz.klenkiven.kmall.product.service.CategoryService;
import xyz.klenkiven.kmall.common.utils.R;



/**
 * 商品三级分类
 *
 * @author klenkiven
 * @email wzl709@outlook.com
 * @date 2021-08-29 16:07:27
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 查出所有的分类列表，按照三级菜单的形式列出来
     */
    @RequestMapping("/list/tree")
    // @RequiresPermissions("product:category:list")
    public R listTree(){
        List<CategoryEntity> entityList = categoryService.listWithTree();
        return R.ok().put("data", entityList);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    // @RequiresPermissions("product:category:info")
    public R info(@PathVariable("catId") Long catId){
		CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("category", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("product:category:save")
    public R save(@RequestBody CategoryEntity category){
		categoryService.save(category);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("product:category:update")
    public R update(@RequestBody CategoryEntity category){
		categoryService.updateDetailById(category);

        return R.ok();
    }

    /**
     * Update Sort Batch
     */
    @RequestMapping("/update/sort")
    // @RequiresPermissions("product:category:update")
    public R updateSort(@RequestBody List<CategoryEntity> categorys){
        categoryService.updateBatchById(categorys);
        return R.ok();
    }

    /**
     * 删除分类列表
     */
    @PostMapping("/delete")
    // @RequiresPermissions("product:category:delete")
    public R delete(@RequestBody Long[] catIds){
        // TODO 检测当前分类是否被引用
		// categoryService.removeByIds(Arrays.asList(catIds));
        categoryService.removeBatch(catIds);
        return R.ok();
    }

}
