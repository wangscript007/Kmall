package xyz.klenkiven.kmall.product.dao;

import org.apache.ibatis.annotations.Param;
import xyz.klenkiven.kmall.product.entity.CategoryBrandRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 品牌分类关联
 * 
 * @author klenkiven
 * @email wzl709@outlook.com
 * @date 2021-08-29 16:07:27
 */
@Mapper
public interface CategoryBrandRelationDao extends BaseMapper<CategoryBrandRelationEntity> {

    /**
     * Update Brand
     * @param brandId brand id
     * @param name brand name
     */
    void updateBrand(@Param("brandId") Long brandId, @Param("name") String name);
}
