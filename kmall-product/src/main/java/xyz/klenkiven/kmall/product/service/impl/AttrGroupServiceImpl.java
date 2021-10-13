package xyz.klenkiven.kmall.product.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mysql.cj.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.klenkiven.kmall.common.utils.PageUtils;
import xyz.klenkiven.kmall.common.utils.Query;

import xyz.klenkiven.kmall.product.dao.AttrGroupDao;
import xyz.klenkiven.kmall.product.entity.AttrGroupEntity;
import xyz.klenkiven.kmall.product.entity.CategoryEntity;
import xyz.klenkiven.kmall.product.service.AttrGroupService;
import xyz.klenkiven.kmall.product.service.CategoryService;


@Service("attrGroupService")
@RequiredArgsConstructor
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    private final CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catalogId) {
        IPage<AttrGroupEntity> resultPage;
        IPage<AttrGroupEntity> paramPage = new Query<AttrGroupEntity>().getPage(params);
        QueryWrapper<AttrGroupEntity> queryWrapper = new QueryWrapper<>();
        if (catalogId != 0) {
            queryWrapper.eq("catelog_id", catalogId);
            String key = (String) params.get("key");
            if (!StringUtils.isNullOrEmpty(key)) {
                queryWrapper.and((condition) -> {
                    condition.eq("attr_group_id", key);
                    condition.or();
                    condition.like("attr_group_name", "%" + key + "%");
                });
            }
        }
        resultPage = this.page(paramPage, queryWrapper);
        return new PageUtils(resultPage);
    }

    @Override
    public AttrGroupEntity getByIdWithCatPath(Long attrGroupId) {
        AttrGroupEntity entityById = this.getById(attrGroupId);
        entityById.setCatalogPath(getCatalogPath(entityById.getCatelogId()));
        return entityById;
    }

    /**
     * Get Catalog Path by Iterating
     * @param catalogId target catalog
     * @return catalog path list
     */
    private List<Long> getCatalogPath(Long catalogId) {
        List<Long> result = new ArrayList<>();
        CategoryEntity catalog;
        do {
            catalog = categoryService.getById(catalogId);
            if (catalog != null) {
                result.add(catalog.getCatId());
                catalogId = catalog.getParentCid();
            }
        } while (catalogId != 0);
        Collections.reverse(result);
        return result;
    }

}