package xyz.klenkiven.kmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.klenkiven.kmall.common.utils.PageUtils;
import xyz.klenkiven.kmall.product.entity.AttrGroupEntity;
import xyz.klenkiven.kmall.product.vo.AttrGroupRespVO;
import xyz.klenkiven.kmall.product.vo.AttrRelationVO;
import xyz.klenkiven.kmall.product.vo.AttrVO;
import xyz.klenkiven.kmall.product.vo.SkuItemVO;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author klenkiven
 * @email wzl709@outlook.com
 * @date 2021-08-29 16:07:27
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * Paging data by catalogId
     * <p>If catalogId is 0, return all the data</p>
     */
    PageUtils queryPage(Map<String, Object> params, Long catalogId);

    /**
     * Get Certain AttrGroup By ID with CatalogPath
     */
    AttrGroupEntity getByIdWithCatPath(Long attrGroupId);

    /**
     * Get all attribute which in certain attribute group
     */
    List<AttrVO> listAllAttrRelation(String attrGroupId);

    /**
     * Remove Attribute relationship in batch
     */
    void removeBatchAttrRelation(List<AttrRelationVO> attrRelationList);

    /**
     * Remove Attribute relationship in batch
     */
    void saveBatchAttrRelation(List<AttrRelationVO> attrRelationList);

    /**
     * Get all attribute groups that belong to certain catalog
     */
    List<AttrGroupRespVO> listAttrGroup(Long catalogId);

    /**
     * Get Base Attribute VO
     */
    List<SkuItemVO.SpuItemBaseGroupAttrVO> getBaseAttrGroup(Long spuId, Long catalogId);
}

