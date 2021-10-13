package xyz.klenkiven.kmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.klenkiven.kmall.common.utils.PageUtils;
import xyz.klenkiven.kmall.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author klenkiven
 * @email wzl709@outlook.com
 * @date 2021-08-29 16:07:27
 */
public interface CategoryService extends IService<CategoryEntity> {

    /**
     * 分页查询数据结果
     * @param params 数据条件
     * @return 分页结果
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 按照属性结构，将所有分类数据列出
     * @return 分类实体列表
     */
    List<CategoryEntity> listWithTree();

    /**
     * 批量逻辑删除分类
     * @param catIds 分类ID数组
     */
    void removeBatch(Long[] catIds);

    /**
     * Update Cascade when name is changed
     * <p>Because of Category and Brand has a relation table and it has
     * redundant data which is their name.</p>
     * @param category category
     */
    void updateDetailById(CategoryEntity category);

    /**
     * Get Catalog Path by Iterating
     * @param catalogId target catalog
     * @return catalog path list
     */
    List<Long> getCatalogPath(Long catalogId);
}

