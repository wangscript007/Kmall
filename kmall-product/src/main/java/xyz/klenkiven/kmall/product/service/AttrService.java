package xyz.klenkiven.kmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.klenkiven.kmall.common.utils.PageUtils;
import xyz.klenkiven.kmall.product.entity.AttrEntity;
import xyz.klenkiven.kmall.product.vo.AttrRespVO;
import xyz.klenkiven.kmall.product.vo.AttrVO;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author klenkiven
 * @email wzl709@outlook.com
 * @date 2021-08-29 16:07:27
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * Save Attribute VO
     * @param attr vo
     */
    void saveVO(AttrVO attr);

    /**
     * 获取分类规格参数
     * @param catalogId category ID
     * @param params params
     * @param attrType attribute type
     * @return Page
     */
    PageUtils queryBasePage(Long catalogId, Map<String, Object> params, String attrType);

    /**
     * Get detail by ID
     * @param attrId attribute ID
     * @return Response VO
     */
    AttrRespVO getDetailById(Long attrId);

    /**
     * Update Attribute VO
     * @param attr attrVo
     */
    void updateVO(AttrVO attr);

    /**
     * Page Attribute(Base Type) that not relate with Attribute Group
     */
    PageUtils pageAttrNoRelation(Map<String, Object> params, Long attrGroupId);

    /**
     * Remove Cascade
     */
    void removeCascadeByIds(List<Long> asList);
}

