package xyz.klenkiven.kmall.product.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.klenkiven.kmall.common.utils.PageUtils;
import xyz.klenkiven.kmall.common.utils.Query;

import xyz.klenkiven.kmall.product.dao.AttrGroupDao;
import xyz.klenkiven.kmall.product.entity.AttrGroupEntity;
import xyz.klenkiven.kmall.product.service.AttrGroupService;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
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
            queryWrapper.and((condition) -> {
                condition.eq("attr_group_id", params.get("key"));
                condition.or();
                condition.like("attr_group_name", "%" + params.get("key") + "%");
            });
        }
        resultPage = this.page(paramPage, queryWrapper);
        return new PageUtils(resultPage);
    }

}