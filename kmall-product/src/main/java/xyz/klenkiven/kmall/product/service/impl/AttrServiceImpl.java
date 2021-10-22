package xyz.klenkiven.kmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import xyz.klenkiven.kmall.common.constant.ProductConstant;
import xyz.klenkiven.kmall.common.utils.PageUtils;
import xyz.klenkiven.kmall.common.utils.Query;

import xyz.klenkiven.kmall.product.dao.AttrAttrgroupRelationDao;
import xyz.klenkiven.kmall.product.dao.AttrDao;
import xyz.klenkiven.kmall.product.dao.AttrGroupDao;
import xyz.klenkiven.kmall.product.dao.CategoryDao;
import xyz.klenkiven.kmall.product.entity.AttrAttrgroupRelationEntity;
import xyz.klenkiven.kmall.product.entity.AttrEntity;
import xyz.klenkiven.kmall.product.entity.AttrGroupEntity;
import xyz.klenkiven.kmall.product.entity.CategoryEntity;
import xyz.klenkiven.kmall.product.service.AttrService;
import xyz.klenkiven.kmall.product.service.CategoryService;
import xyz.klenkiven.kmall.product.vo.AttrRespVO;
import xyz.klenkiven.kmall.product.vo.AttrVO;


@Service("attrService")
@RequiredArgsConstructor
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    private final AttrAttrgroupRelationDao attrAttrgroupRelationDao;
    private final AttrGroupDao attrGroupDao;
    private final CategoryDao categoryDao;

    private final CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveVO(AttrVO attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        // After saving data, mbp will complete entity automatically.
        this.save(attrEntity);

        if (ProductConstant.AttrType.ATTR_TYPE_SALE.getCode().equals(attrEntity.getAttrType()) ||
            attr.getAttrGroupId() == null) {
            return;
        }
        // Save Relation
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        relationEntity.setAttrId(attrEntity.getAttrId());
        relationEntity.setAttrGroupId(attr.getAttrGroupId());
        attrAttrgroupRelationDao.insert(relationEntity);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public PageUtils queryBasePage(Long catalogId, Map<String, Object> params, String attrType) {
        IPage<AttrEntity> page = new Query<AttrEntity>().getPage(params);
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("attr_type",
                "base".equals(attrType)?
                        ProductConstant.AttrType.ATTR_TYPE_BASE.getCode():
                        ProductConstant.AttrType.ATTR_TYPE_SALE.getCode()
        );
        if (catalogId != 0) {
            queryWrapper.eq("catelog_id", catalogId);
        }

        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.and((query) -> {
                query.eq("attr_id", key);
                query.or();
                query.like("attr_name", "%" + key + "%");
            });
        }
        IPage<AttrEntity> iPage = this.page(page, queryWrapper);

        PageUtils result = new PageUtils(iPage);
        List<AttrEntity> records = iPage.getRecords();
        List<AttrRespVO> collect = records.stream()
                .map((record) -> {
                    AttrRespVO vo = new AttrRespVO();
                    BeanUtils.copyProperties(record, vo);

                    if ("base".equals(attrType)) {
                        // SET Attribute Group Name
                        AttrAttrgroupRelationEntity relationEntity = attrAttrgroupRelationDao.selectOne(
                                new QueryWrapper<AttrAttrgroupRelationEntity>()
                                        .eq("attr_id", record.getAttrId())
                        );
                        if (relationEntity != null) {
                            AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(relationEntity.getAttrGroupId());
                            if (attrGroupEntity != null) {
                                vo.setGroupName(attrGroupEntity.getAttrGroupName());
                            }
                        }
                    }

                    // SET Catalog Name
                    CategoryEntity categoryEntity = categoryDao.selectById(record.getCatelogId());
                    if (categoryEntity != null) {
                        vo.setCatelogName(categoryEntity.getName());
                    }

                    return vo;
                })
                .collect(Collectors.toList());
        result.setList(collect);
        return result;
    }

    @Override
    public AttrRespVO getDetailById(Long attrId) {
        AttrRespVO respVO = new AttrRespVO();

        AttrEntity byId = this.getById(attrId);
        BeanUtils.copyProperties(byId, respVO);

        if (ProductConstant.AttrType.ATTR_TYPE_BASE.getCode().equals(byId.getAttrType())) {
            // SET Attribute ID
            AttrAttrgroupRelationEntity relationEntity = attrAttrgroupRelationDao.selectOne(
                    new QueryWrapper<AttrAttrgroupRelationEntity>()
                            .eq("attr_id", attrId)
            );
            if (relationEntity != null) {
                respVO.setAttrGroupId(relationEntity.getAttrGroupId());
            }
        }

        // SET Category Path
        respVO.setCatelogPath(categoryService.getCatalogPath(respVO.getCatelogId()));

        return respVO;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void updateVO(AttrVO attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        // After saving data, mbp will complete entity automatically.
        this.updateById(attrEntity);

        if (ProductConstant.AttrType.ATTR_TYPE_SALE.getCode().equals(attrEntity.getAttrType())) {
            return;
        }
        // Save Relation
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        relationEntity.setAttrId(attrEntity.getAttrId());
        relationEntity.setAttrGroupId(attr.getAttrGroupId());
        long count = attrAttrgroupRelationDao.selectCount(
                new QueryWrapper<AttrAttrgroupRelationEntity>()
                        .eq("attr_id", relationEntity.getAttrId())
        );
        if (count > 0) {
            attrAttrgroupRelationDao.update(
                    relationEntity,
                    new UpdateWrapper<AttrAttrgroupRelationEntity>()
                            .eq("attr_id", relationEntity.getAttrId())
            );
        } else {
            attrAttrgroupRelationDao.insert(relationEntity);
        }
    }

    @Override
    public PageUtils pageAttrNoRelation(Map<String, Object> params, Long attrGroupId) {
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrGroupId);
        Long catalogId = attrGroupEntity.getCatelogId();
        List<Long> exceptAttrIdList=
                attrAttrgroupRelationDao.selectList(new QueryWrapper<>()).stream()
                        .map(AttrAttrgroupRelationEntity::getAttrId)
                        .collect(Collectors.toList());
        Page<AttrEntity> page = this.page(
                new Page<>(),
                new QueryWrapper<AttrEntity>()
                        .eq("catelog_id", catalogId)
                        .eq("attr_type", ProductConstant.AttrType.ATTR_TYPE_BASE.getCode())
                        .notIn(exceptAttrIdList.size() != 0, "attr_id", exceptAttrIdList)
        );
        return new PageUtils(page);
    }

    @Override
    public void removeCascadeByIds(List<Long> asList) {
        this.removeByIds(asList);

        // Remove Relationships
        /* Attr - AttrGroup Relation */
        asList.forEach((attrId) -> {
                    attrAttrgroupRelationDao.delete(
                            new QueryWrapper<AttrAttrgroupRelationEntity>()
                                    .eq("attr_id", attrId)
                    );
                });
    }

    @Override
    public List<Long> listSearchAttributeId(List<Long> attrIdList) {
        return baseMapper.listSearchAttributeId(attrIdList);
    }

}