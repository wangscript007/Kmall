package xyz.klenkiven.kmall.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.klenkiven.kmall.common.utils.PageUtils;
import xyz.klenkiven.kmall.common.utils.Query;

import xyz.klenkiven.kmall.product.dao.SkuInfoDao;
import xyz.klenkiven.kmall.product.entity.SkuImagesEntity;
import xyz.klenkiven.kmall.product.entity.SkuInfoEntity;
import xyz.klenkiven.kmall.product.entity.SpuInfoDescEntity;
import xyz.klenkiven.kmall.product.service.*;
import xyz.klenkiven.kmall.product.vo.SkuItemVO;


@Service("skuInfoService")
@RequiredArgsConstructor
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    private final SkuImagesService skuImagesService;
    private final SpuInfoDescService spuInfoDescService;
    private final AttrGroupService attrGroupService;
    private final SkuSaleAttrValueService skuSaleAttrValueService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> skuInfoEntityQueryWrapper = new QueryWrapper<>();

        String catalogId = (String) params.get("catelogId");
        skuInfoEntityQueryWrapper.eq(!StringUtils.isEmpty(catalogId), "catalog_id", catalogId);
        String brandId = (String) params.get("brandId");
        skuInfoEntityQueryWrapper.eq(!StringUtils.isEmpty(catalogId), "brand_id", brandId);
        String key = (String) params.get("key");
        skuInfoEntityQueryWrapper.and(!StringUtils.isBlank(key), (wrapper) ->
                wrapper.eq("sku_id", key).or().like("sku_name", key));

        try {
            BigDecimal min = new BigDecimal((String) params.get("min"));
            skuInfoEntityQueryWrapper.ge(min.compareTo(BigDecimal.ZERO) > 0, "price", min);
            BigDecimal max = new BigDecimal((String) params.get("max"));
            skuInfoEntityQueryWrapper.le(max.compareTo(BigDecimal.ZERO) > 0, "price", max);
        } catch (Exception e) {
            // Do nothing
        }

        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                skuInfoEntityQueryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public SkuItemVO item(Long skuId) {
        SkuItemVO skuItemVO = new SkuItemVO();
        // 1. SKU basic info `pms_sku_info`
        SkuInfoEntity info = getById(skuId);
        Long spuId = info.getSpuId();
        Long catalogId = info.getCatalogId();
        skuItemVO.setInfo(info);

        // 2. SKU images `pms_sku_images`
        List<SkuImagesEntity> imgList = skuImagesService.getImagesBySkuId(skuId);
        skuItemVO.setImages(imgList);

        // 3. SPU sale attribute combination
        List<SkuItemVO.SkuItemSaleAttrVO> saleVOs = skuSaleAttrValueService.getSaleAttr(spuId);
        skuItemVO.setSaleAttrs(saleVOs);

        // 4. SPU detail information
        SpuInfoDescEntity spuDesc = spuInfoDescService.getById(spuId);
        skuItemVO.setDescription(spuDesc);

        // 5. SPU specification attribute info
        List<SkuItemVO.SpuItemBaseGroupAttrVO> baseVOs = attrGroupService.getBaseAttrGroup(spuId, catalogId);
        skuItemVO.setGroupAttrs(baseVOs);


        return skuItemVO;
    }

}