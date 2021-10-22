package xyz.klenkiven.kmall.product.dao;

import org.apache.ibatis.annotations.Param;
import xyz.klenkiven.kmall.product.entity.AttrEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 商品属性
 * 
 * @author klenkiven
 * @email wzl709@outlook.com
 * @date 2021-08-29 16:07:27
 */
@Mapper
public interface AttrDao extends BaseMapper<AttrEntity> {

    /**
     * Get all attr ID which can be used to search
     */
    List<Long> listSearchAttributeId(@Param("attrIds") List<Long> attrIdList);
}
