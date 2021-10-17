package xyz.klenkiven.kmall.ware.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import xyz.klenkiven.kmall.common.constant.WareConstant;
import xyz.klenkiven.kmall.common.utils.PageUtils;
import xyz.klenkiven.kmall.common.utils.Query;

import xyz.klenkiven.kmall.ware.dao.PurchaseDao;
import xyz.klenkiven.kmall.ware.entity.PurchaseDetailEntity;
import xyz.klenkiven.kmall.ware.entity.PurchaseEntity;
import xyz.klenkiven.kmall.ware.service.PurchaseDetailService;
import xyz.klenkiven.kmall.ware.service.PurchaseService;
import xyz.klenkiven.kmall.ware.service.WareSkuService;
import xyz.klenkiven.kmall.ware.vo.MergeVO;
import xyz.klenkiven.kmall.ware.vo.PurchaseDoneVO;
import xyz.klenkiven.kmall.ware.vo.PurchaseItemDoneVO;


@Service("purchaseService")
@RequiredArgsConstructor
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    private final PurchaseDetailService purchaseDetailService;
    private final WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryUnreceivedPage(Map<String, Object> params) {
        QueryWrapper<PurchaseEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("status", 0, 1);
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                queryWrapper
        );
        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void mergePurchase(MergeVO mergeVO) {
        Long purchaseId = mergeVO.getPurchaseId();

        // If purchase is not provide, create new one
        if (purchaseId == null) {
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setStatus(WareConstant.PurchaseStatus.CREATED.getCode());
            purchaseEntity.setPriority(0);
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            this.save(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        }

        List<Long> items = mergeVO.getItems();
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> collect = items.stream()
                .map((item) -> {
                    PurchaseDetailEntity purchaseDetail = new PurchaseDetailEntity();
                    purchaseDetail.setId(item);
                    purchaseDetail.setPurchaseId(finalPurchaseId);
                    purchaseDetail.setStatus(WareConstant.PurchaseDetailStatus.ASSIGNED.getCode());
                    return purchaseDetail;
                }).collect(Collectors.toList());

        purchaseDetailService.updateBatchById(collect);

        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseId);
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void receivePurchase(List<Long> purchaseList) {
        // Filter
        List<PurchaseEntity> collect = purchaseList.stream()
                .map(this::getById)
                .filter((entity) ->
                        Objects.equals(entity.getStatus(), WareConstant.PurchaseStatus.CREATED.getCode()) ||
                                Objects.equals(entity.getStatus(), WareConstant.PurchaseStatus.ASSIGNED.getCode())
                )
                .peek(purchaseEntity -> {
                    purchaseEntity.setStatus(WareConstant.PurchaseStatus.RECEIVED.getCode());
                    purchaseEntity.setUpdateTime(new Date());
                }).collect(Collectors.toList());

        // Save Purchase
        this.updateBatchById(collect);

        // Save Purchase Detail
        collect.forEach((purchaseEntity -> {
            List<PurchaseDetailEntity> detailEntities =
                    purchaseDetailService.listByPurchaseId(purchaseEntity.getId());
            detailEntities = detailEntities.stream()
                    .peek(detailEntity -> detailEntity.setStatus(WareConstant.PurchaseDetailStatus.BUYING.getCode()))
                    .collect(Collectors.toList());
            purchaseDetailService.updateBatchById(detailEntities);
        }));
    }

    @Override
    public void done(PurchaseDoneVO purchaseDoneVO) {
        boolean flag = true;

        // Update Purchase Items
        List<PurchaseDetailEntity> purchaseDetailEntityList = new ArrayList<>();
        for (PurchaseItemDoneVO item : purchaseDoneVO.getItems()) {
            PurchaseDetailEntity purchaseDetail = new PurchaseDetailEntity();
            purchaseDetail.setId(item.getItemId());
            purchaseDetail.setStatus(item.getStatus());
            purchaseDetailEntityList.add(purchaseDetail);

            if (Objects.equals(item.getStatus(), WareConstant.PurchaseDetailStatus.HAS_ERROR.getCode())) {
                flag = false;
            } else {
                // Add Stock
                PurchaseDetailEntity byId = purchaseDetailService.getById(item.getItemId());
                wareSkuService.addStock(byId.getWareId(), byId.getSkuId(), byId.getSkuNum());
            }
        }
        purchaseDetailService.updateBatchById(purchaseDetailEntityList);

        // Update Purchase
        PurchaseEntity purchaseEntity = this.getById(purchaseDoneVO.getId());
        purchaseEntity.setStatus(
                flag?
                WareConstant.PurchaseStatus.FINISHED.getCode() :
                WareConstant.PurchaseStatus.HAS_ERROR.getCode()
        );
        this.updateById(purchaseEntity);
    }

}