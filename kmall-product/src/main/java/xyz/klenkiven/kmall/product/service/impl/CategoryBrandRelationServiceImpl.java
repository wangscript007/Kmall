package xyz.klenkiven.kmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.klenkiven.kmall.common.utils.PageUtils;
import xyz.klenkiven.kmall.common.utils.Query;

import xyz.klenkiven.kmall.product.dao.BrandDao;
import xyz.klenkiven.kmall.product.dao.CategoryBrandRelationDao;
import xyz.klenkiven.kmall.product.dao.CategoryDao;
import xyz.klenkiven.kmall.product.entity.BrandEntity;
import xyz.klenkiven.kmall.product.entity.CategoryBrandRelationEntity;
import xyz.klenkiven.kmall.product.entity.CategoryEntity;
import xyz.klenkiven.kmall.product.service.CategoryBrandRelationService;
import xyz.klenkiven.kmall.product.vo.BrandRespVO;


@Service("categoryBrandRelationService")
@RequiredArgsConstructor
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity>
        implements CategoryBrandRelationService {

    private final CategoryDao categoryDao;
    private final BrandDao brandDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation) {
        BrandEntity brand = brandDao.selectById(categoryBrandRelation.getBrandId());
        CategoryEntity category = categoryDao.selectById(categoryBrandRelation.getCatelogId());
        categoryBrandRelation.setBrandName(brand.getName());
        categoryBrandRelation.setCatelogName(category.getName());
        this.save(categoryBrandRelation);
    }

    @Override
    public void updateCategory(Long catId, String name) {
        CategoryBrandRelationEntity categoryBrandRelation = new CategoryBrandRelationEntity();
        categoryBrandRelation.setCatelogId(catId);
        categoryBrandRelation.setCatelogName(name);
        this.update(categoryBrandRelation,
                new UpdateWrapper<CategoryBrandRelationEntity>().eq("catelog_id", catId));
    }

    @Override
    public void updateBrand(Long brandId, String name) {
        baseMapper.updateBrand(brandId, name);
    }

    @Override
    public List<BrandRespVO> listAllBrands(Long catId) {
        return this.list(new QueryWrapper<CategoryBrandRelationEntity>().eq("catelog_id", catId)).stream()
                .map((item) -> {
                    BrandRespVO ret = new BrandRespVO();
                    BeanUtils.copyProperties(item, ret);
                    return ret;
                }).collect(Collectors.toList());
    }

}