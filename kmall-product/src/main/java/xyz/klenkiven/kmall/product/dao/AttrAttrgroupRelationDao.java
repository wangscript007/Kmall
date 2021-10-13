package xyz.klenkiven.kmall.product.dao;

import org.apache.ibatis.annotations.Param;
import xyz.klenkiven.kmall.product.entity.AttrAttrgroupRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.klenkiven.kmall.product.vo.AttrRelationVO;

import java.util.List;

/**
 * 属性&属性分组关联
 * 
 * @author klenkiven
 * @email wzl709@outlook.com
 * @date 2021-08-29 16:07:27
 */
@Mapper
public interface AttrAttrgroupRelationDao extends BaseMapper<AttrAttrgroupRelationEntity> {

    /**
     * Remove Attribute relationship in batch
     */
    void deleteBatchAttrRelation(@Param("attrRelationList") List<AttrRelationVO> attrRelationList);
}
