package xyz.klenkiven.kmall.product.service.impl;

import com.mysql.cj.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.klenkiven.kmall.common.utils.PageUtils;
import xyz.klenkiven.kmall.common.utils.Query;

import xyz.klenkiven.kmall.product.dao.AttrAttrgroupRelationDao;
import xyz.klenkiven.kmall.product.dao.AttrDao;
import xyz.klenkiven.kmall.product.dao.AttrGroupDao;
import xyz.klenkiven.kmall.product.entity.AttrAttrgroupRelationEntity;
import xyz.klenkiven.kmall.product.entity.AttrEntity;
import xyz.klenkiven.kmall.product.entity.AttrGroupEntity;
import xyz.klenkiven.kmall.product.service.AttrGroupService;
import xyz.klenkiven.kmall.product.service.CategoryService;
import xyz.klenkiven.kmall.product.vo.AttrRelationVO;
import xyz.klenkiven.kmall.product.vo.AttrVO;


@Service("attrGroupService")
@RequiredArgsConstructor
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    private final CategoryService categoryService;
    private final AttrAttrgroupRelationDao relationDao;
    private final AttrDao attrDao;

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
        entityById.setCatalogPath(categoryService.getCatalogPath(entityById.getCatelogId()));
        return entityById;
    }

    @Override
    public List<AttrVO> listAllAttrRelation(String attrGroupId) {
        List<AttrAttrgroupRelationEntity> relationEntities = relationDao.selectList(
                new QueryWrapper<AttrAttrgroupRelationEntity>()
                        .eq("attr_group_id", attrGroupId)
        );
        List<Long> attrIdList = relationEntities.stream()
                .map(AttrAttrgroupRelationEntity::getAttrId)
                .collect(Collectors.toList());
        List<AttrVO> result = new ArrayList<>();

        if (attrIdList.size() != 0) {
            result = attrDao.selectList(
                    new QueryWrapper<AttrEntity>()
                    .in("attr_id", attrIdList)
            ).stream().map((item) -> {
                AttrVO attrVO = new AttrVO();
                BeanUtils.copyProperties(item, attrVO);
                return attrVO;
            }).collect(Collectors.toList());
        }
        return result;
    }

    @Override
    public void removeBatchAttrRelation(List<AttrRelationVO> attrRelationList) {
        relationDao.deleteBatchAttrRelation(attrRelationList);
    }

}