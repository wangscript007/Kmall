package xyz.klenkiven.kmall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.klenkiven.kmall.common.utils.PageUtils;
import xyz.klenkiven.kmall.common.utils.Query;

import xyz.klenkiven.kmall.product.dao.SkuSaleAttrValueDao;
import xyz.klenkiven.kmall.product.entity.SkuSaleAttrValueEntity;
import xyz.klenkiven.kmall.product.service.SkuSaleAttrValueService;
import xyz.klenkiven.kmall.product.vo.SkuItemVO;


@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuSaleAttrValueEntity> page = this.page(
                new Query<SkuSaleAttrValueEntity>().getPage(params),
                new QueryWrapper<SkuSaleAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuItemVO.SkuItemSaleAttrVO> getSaleAttr(Long spuId) {
        SkuSaleAttrValueDao skuSaleAttrValueDao = this.baseMapper;
        return skuSaleAttrValueDao.getSaleAttr(spuId);
    }

}