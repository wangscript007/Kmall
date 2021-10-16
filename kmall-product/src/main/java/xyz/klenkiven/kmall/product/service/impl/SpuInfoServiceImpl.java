package xyz.klenkiven.kmall.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import xyz.klenkiven.kmall.common.to.SkuReductionTO;
import xyz.klenkiven.kmall.common.to.SpuBoundsTO;
import xyz.klenkiven.kmall.common.utils.PageUtils;
import xyz.klenkiven.kmall.common.utils.Query;

import xyz.klenkiven.kmall.common.utils.R;
import xyz.klenkiven.kmall.product.dao.SpuInfoDao;
import xyz.klenkiven.kmall.product.entity.*;
import xyz.klenkiven.kmall.product.feign.CouponFeignService;
import xyz.klenkiven.kmall.product.service.*;
import xyz.klenkiven.kmall.product.vo.*;


@Service("spuInfoService")
@RequiredArgsConstructor
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    private final SpuInfoDescService spuInfoDescService;
    private final SpuImagesService imagesService;
    private final ProductAttrValueService productAttrValueService;
    private final SkuInfoService skuInfoService;
    private final SkuImagesService skuImagesService;
    private final SkuSaleAttrValueService skuSaleAttrValueService;

    private final CouponFeignService couponFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void savSpuInfo(SpuSaveVO spuSaveVO) {
        // 1. Save SPU basic info:              pms => pms_spu_info
        SpuInfoEntity spuInfo = new SpuInfoEntity();
        BeanUtils.copyProperties(spuSaveVO, spuInfo);
        spuInfo.setUpdateTime(new Date());
        spuInfo.setCreateTime(new Date());
        this.save(spuInfo);

        // 2. Save SPU describe message:        pms => pms_spu_info_desc
        List<String> decript = spuSaveVO.getDecript();
        SpuInfoDescEntity desc = new SpuInfoDescEntity();
        desc.setSpuId(spuInfo.getId());
        desc.setDecript(decript != null ? String.join(",", decript) : "");
        spuInfoDescService.save(desc);

        // 3. Save SPU images:                  pms => pms_spu_images
        List<String> images = spuSaveVO.getImages();
        imagesService.saveImages(spuInfo.getId(), images);

        // 4. Save SPU attribute:               pms => pms_product_attr_value
        List<BaseAttrs> baseAttrs = spuSaveVO.getBaseAttrs();
        productAttrValueService.saveBaseAttrs(spuInfo.getId(), baseAttrs);

        // 5. Save SPU bounds[RPC]:             sms => sms_spu_bounds
        SpuBoundsTO spuBoundsTO = new SpuBoundsTO();
        BeanUtils.copyProperties(spuSaveVO.getBounds(), spuBoundsTO);
        spuBoundsTO.setSpuId(spuInfo.getId());
        R saveSpuBounds = couponFeignService.saveSpuBounds(spuBoundsTO);
        if (saveSpuBounds.getCode() != 0) {
            log.error("[RPC ERROR] SPU Bounds Save Failed: " + saveSpuBounds.get("message"));
        }

        // 6. Save SKU
        List<Skus> skus = spuSaveVO.getSkus();
        if (skus == null || skus.size() == 0) {
            return;
        }
        for (Skus sku : skus) {
            // 6.1 Save SKU base Info               pms => pms_sku_info
            SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
            BeanUtils.copyProperties(sku, skuInfoEntity);
            skuInfoEntity.setSaleCount(0L);
            // Redundant SPU Info
            skuInfoEntity.setSpuId(spuInfo.getId());
            skuInfoEntity.setCatalogId(spuInfo.getCatalogId());
            skuInfoEntity.setBrandId(spuInfo.getBrandId());
            // Get Default Img
            List<Images> imagesList = sku.getImages();
            if (imagesList != null && imagesList.size() != 0) {
                imagesList.forEach((img) -> {
                    if (img.getDefaultImg() == 1) {
                        skuInfoEntity.setSkuDefaultImg(img.getImgUrl());
                    }
                });
            }
            skuInfoService.save(skuInfoEntity);

            // 6.2 Save SKU images:                 pms => pms_sku_images
            if (imagesList != null && imagesList.size() != 0) {
                imagesList.forEach((img) -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    if (!StringUtils.isBlank(img.getImgUrl())) {
                        BeanUtils.copyProperties(img, skuImagesEntity);
                        skuImagesEntity.setSkuId(skuInfoEntity.getSkuId());
                        skuImagesService.save(skuImagesEntity);
                    }
                });
            }

            // 6.3 Save SKU attribute and value:    pms => pms_sku_sale_attr_value
            List<Attr> attrList = sku.getAttr();
            if (attrList != null && attrList.size() != 0) {
                attrList.forEach(attr -> {
                    SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(attr, skuSaleAttrValueEntity);
                    skuSaleAttrValueEntity.setSkuId(skuInfoEntity.getSkuId());
                    skuSaleAttrValueService.save(skuSaleAttrValueEntity);
                });
            }

            // 6.4 Save SKU full reduction[RPC]:    sms => sms_sku_full_reduction
            // 6.5 Save SKU ladder[RPC]:            sms => sms_sku_ladder
            // 6.6 Save SKU member price[RPC]:      sms => sms_member_price
            SkuReductionTO skuReductionTO = new SkuReductionTO();
            BeanUtils.copyProperties(sku, skuReductionTO);
            skuReductionTO.setSkuId(skuInfoEntity.getSkuId());
            R skuReduction = couponFeignService.saveSkuReduction(skuReductionTO);
            if (skuReduction.getCode() != 0) {
                log.error("[RPC ERROR] SKU Reduction Save Failed: " + skuReduction.get("message"));
            }
        }
    }

}