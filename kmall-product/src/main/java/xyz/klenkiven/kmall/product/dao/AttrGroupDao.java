package xyz.klenkiven.kmall.product.dao;

import xyz.klenkiven.kmall.product.entity.AttrGroupEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.klenkiven.kmall.product.vo.SkuItemVO;

import java.util.List;

/**
 * 属性分组
 * 
 * @author klenkiven
 * @email wzl709@outlook.com
 * @date 2021-08-29 16:07:27
 */
@Mapper
public interface AttrGroupDao extends BaseMapper<AttrGroupEntity> {

    /**
     * Get base Attribute by Group
     */
    List<SkuItemVO.SpuItemBaseGroupAttrVO> getBaseAttrGroup(Long spuId, Long catalogId);
}
