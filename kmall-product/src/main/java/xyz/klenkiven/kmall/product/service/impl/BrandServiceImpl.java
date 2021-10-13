package xyz.klenkiven.kmall.product.service.impl;

import com.mysql.cj.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.klenkiven.kmall.common.utils.PageUtils;
import xyz.klenkiven.kmall.common.utils.Query;

import xyz.klenkiven.kmall.product.dao.BrandDao;
import xyz.klenkiven.kmall.product.entity.BrandEntity;
import xyz.klenkiven.kmall.product.service.BrandService;
import xyz.klenkiven.kmall.product.service.CategoryBrandRelationService;


@Service("brandService")
@RequiredArgsConstructor
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    private final CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<BrandEntity> resultPage;
        IPage<BrandEntity> paramPage = new Query<BrandEntity>().getPage(params);
        QueryWrapper<BrandEntity> queryWrapper = new QueryWrapper<>();

        String key = (String) params.get("key");
        if (!StringUtils.isNullOrEmpty(key)) {
            queryWrapper.eq("brand_id", key);
            queryWrapper.or();
            queryWrapper.like("name", "%" + key + "%");
        }

        resultPage = this.page(paramPage, queryWrapper);
        return new PageUtils(resultPage);
    }

    @Override
    public void updateDetailById(BrandEntity brand) {
        if (!StringUtils.isNullOrEmpty(brand.getName())) {
            categoryBrandRelationService.updateBrand(brand.getBrandId(), brand.getName());
            // TODO Other Relation
        }
        this.updateById(brand);
    }

}