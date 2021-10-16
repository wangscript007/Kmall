package xyz.klenkiven.kmall.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.klenkiven.kmall.common.utils.PageUtils;
import xyz.klenkiven.kmall.common.utils.Query;

import xyz.klenkiven.kmall.product.dao.AttrDao;
import xyz.klenkiven.kmall.product.dao.ProductAttrValueDao;
import xyz.klenkiven.kmall.product.entity.AttrEntity;
import xyz.klenkiven.kmall.product.entity.ProductAttrValueEntity;
import xyz.klenkiven.kmall.product.service.ProductAttrValueService;
import xyz.klenkiven.kmall.product.vo.BaseAttrs;


@Service("productAttrValueService")
@RequiredArgsConstructor
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {

    private final AttrDao attrDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductAttrValueEntity> page = this.page(
                new Query<ProductAttrValueEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveBaseAttrs(Long id, List<BaseAttrs> baseAttrs) {
        if (baseAttrs == null || baseAttrs.size() == 0) {
            return;
        }

        List<ProductAttrValueEntity> collect = baseAttrs.stream()
                .map(baseAttr -> {
                    ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
                    productAttrValueEntity.setSpuId(id);
                    productAttrValueEntity.setAttrId((long) baseAttr.getAttrId());
                    productAttrValueEntity.setAttrValue(baseAttr.getAttrValues());
                    productAttrValueEntity.setQuickShow(baseAttr.getShowDesc());

                    AttrEntity attrEntity = attrDao.selectById(baseAttr.getAttrId());
                    productAttrValueEntity.setAttrName(attrEntity.getAttrName());

                    return productAttrValueEntity;
                }).collect(Collectors.toList());
        this.saveBatch(collect);
    }

}