package xyz.klenkiven.kmall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.klenkiven.common.utils.PageUtils;
import xyz.klenkiven.common.utils.Query;

import xyz.klenkiven.kmall.product.dao.CategoryDao;
import xyz.klenkiven.kmall.product.entity.CategoryEntity;
import xyz.klenkiven.kmall.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);

        return categoryEntities.stream()
                .filter(categoryEntity -> categoryEntity.getCatLevel() == 1)
                .peek(categoryEntity ->
                        categoryEntity.setChildren(getChildrenCategory(categoryEntity, categoryEntities)))
                // 比较排序
                .sorted((c1, c2) -> c1.getSort() - c2.getSort())
                .collect(Collectors.toList());
    }

    /**
     * 获取此分类下的所有子分类，以及递归子分类
     * @param parent 父分类
     * @param allCategory 所有分类
     * @return 子分类
     */
    private List<CategoryEntity> getChildrenCategory(CategoryEntity parent, List<CategoryEntity> allCategory) {
        return allCategory.stream()
                .filter(c -> c.getParentCid().equals(parent.getCatId()))
                .peek(c -> c.setChildren(getChildrenCategory(c, allCategory)))
                .sorted(Comparator.comparingInt(CategoryEntity::getSort))
                .collect(Collectors.toList());
    }

}