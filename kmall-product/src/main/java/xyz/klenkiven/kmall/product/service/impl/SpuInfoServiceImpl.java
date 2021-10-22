package xyz.klenkiven.kmall.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import xyz.klenkiven.kmall.common.constant.ProductConstant;
import xyz.klenkiven.kmall.common.to.SkuHasStockTO;
import xyz.klenkiven.kmall.common.to.SkuReductionTO;
import xyz.klenkiven.kmall.common.to.SpuBoundsTO;
import xyz.klenkiven.kmall.common.to.elasticsearch.SkuESModel;
import xyz.klenkiven.kmall.common.utils.PageUtils;
import xyz.klenkiven.kmall.common.utils.Query;

import xyz.klenkiven.kmall.common.utils.R;
import xyz.klenkiven.kmall.product.dao.SpuInfoDao;
import xyz.klenkiven.kmall.product.entity.*;
import xyz.klenkiven.kmall.product.feign.CouponFeignService;
import xyz.klenkiven.kmall.product.feign.SearchFeignService;
import xyz.klenkiven.kmall.product.feign.WareFeignService;
import xyz.klenkiven.kmall.product.service.*;
import xyz.klenkiven.kmall.product.vo.*;

import javax.xml.catalog.Catalog;
import javax.xml.catalog.CatalogException;


@Service("spuInfoService")
@RequiredArgsConstructor
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    private final SpuInfoDescService spuInfoDescService;
    private final SpuImagesService imagesService;
    private final AttrService attrService;
    private final ProductAttrValueService productAttrValueService;
    private final SkuInfoService skuInfoService;
    private final SkuImagesService skuImagesService;
    private final SkuSaleAttrValueService skuSaleAttrValueService;
    private final BrandService brandService;
    private final CategoryService categoryService;

    private final CouponFeignService couponFeignService;
    private final WareFeignService wareFeignService;
    private final SearchFeignService searchFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();

        String catalogId = (String) params.get("catelogId");
        if (!StringUtils.isEmpty(catalogId)) {
            wrapper.eq("catalog_id", catalogId);
        }

        String brandId = (String) params.get("brandId");
        if (!StringUtils.isEmpty(brandId)) {
            wrapper.eq("brand_id", brandId);
        }

        String status = (String) params.get("status");
        if (!StringUtils.isEmpty(status)) {
            wrapper.eq("publish_status", status);
        }

        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            wrapper.and((w) -> {
                w.eq("id", key).or().like("spu_name", key);
            });
        }

        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                wrapper
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
        R<?> saveSpuBounds = couponFeignService.saveSpuBounds(spuBoundsTO);
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
            R<?> skuReduction = couponFeignService.saveSkuReduction(skuReductionTO);
            if (skuReduction.getCode() != 0) {
                log.error("[RPC ERROR] SKU Reduction Save Failed: " + skuReduction.get("message"));
            }
        }
    }

    @Override
    public void productUp(Long spuId) {
        List<SkuESModel> upProductList = new ArrayList<>();

        // 1. Complete Product Data
        List<SkuInfoEntity> skuList =
                skuInfoService.list(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));
        List<Long> skuIdList = skuList.stream()
                .map(SkuInfoEntity::getSkuId)
                .collect(Collectors.toList());

        // Get All SPU Attributes which can be search for
        List<ProductAttrValueEntity> attrEntities = productAttrValueService.listBaseAttrForSpu(spuId);
        List<Long> attrIdList =
                attrEntities.stream()
                        .map(ProductAttrValueEntity::getAttrId)
                        .collect(Collectors.toList());
        List<Long> searchAttrIdList = attrService.listSearchAttributeId(attrIdList);
        List<SkuESModel.Attrs> attrsList = attrEntities.stream()
                .filter(attr -> searchAttrIdList.contains(attr.getAttrId()))
                .map(attr -> {
                    SkuESModel.Attrs attrs = new SkuESModel.Attrs();
                    BeanUtils.copyProperties(attr, attrs);
                    return attrs;
                })
                .collect(Collectors.toList());

        // Get SKU Has Stock Status
        Map<Long, Boolean> skuStockMap = new HashMap<>();
        try {
            R<List<SkuHasStockTO>> skuHasStock = wareFeignService.getSkuHasStock(skuIdList);
            skuStockMap = skuHasStock.getData().stream()
                    .collect(Collectors.toMap(SkuHasStockTO::getSkuId, SkuHasStockTO::getHasStock));
        } catch (Exception e) {
            log.error("[ERROR] Ware SKU Stock Query Error: Caused by {}", e);
        }

        // 2. For each every SKU Info
        for (SkuInfoEntity skuInfo : skuList) {
            SkuESModel esModel = new SkuESModel();

            // 2.1 Fill ESModel with skuInfo
            BeanUtils.copyProperties(skuInfo, esModel);
            esModel.setSkuPrice(skuInfo.getPrice());
            esModel.setSkuImg(skuInfo.getSkuDefaultImg());

            // 2.2 Fill ESModel with Brand and Category
            BrandEntity brandEntity = brandService.getById(skuInfo.getBrandId());
            esModel.setBrandName(brandEntity.getName());
            esModel.setBrandImg(brandEntity.getLogo());
            CategoryEntity categoryEntity = categoryService.getById(skuInfo.getCatalogId());
            esModel.setCatalogName(categoryEntity.getName());

            // 2.3 [RPC] Has Stock
            esModel.setHasStock(skuStockMap.getOrDefault(skuInfo.getSkuId(), true));

            // 2.4 Hot Score
            esModel.setHotScore(0L);

            // 2.5 Attributes List
            esModel.setAttrs(attrsList);

            upProductList.add(esModel);
        }

        // 3. [RPC] Save Product List to ElasticSearch
        R<?> r = searchFeignService.productStatusUp(upProductList);
        if (r.getCode() == 0) {
            // Success
            baseMapper.updateSpuStatus(spuId, ProductConstant.SpuStatus.SPU_UP.getCode());
        } else {
            // Failure: Retry, Idempotence and so on
        }
    }

}