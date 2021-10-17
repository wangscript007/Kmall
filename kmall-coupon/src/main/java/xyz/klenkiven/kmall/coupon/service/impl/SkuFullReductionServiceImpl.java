package xyz.klenkiven.kmall.coupon.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import xyz.klenkiven.kmall.common.to.MemberPrice;
import xyz.klenkiven.kmall.common.to.SkuReductionTO;
import xyz.klenkiven.kmall.common.utils.PageUtils;
import xyz.klenkiven.kmall.common.utils.Query;

import xyz.klenkiven.kmall.coupon.dao.SkuFullReductionDao;
import xyz.klenkiven.kmall.coupon.entity.MemberPriceEntity;
import xyz.klenkiven.kmall.coupon.entity.SkuFullReductionEntity;
import xyz.klenkiven.kmall.coupon.entity.SkuLadderEntity;
import xyz.klenkiven.kmall.coupon.service.MemberPriceService;
import xyz.klenkiven.kmall.coupon.service.SkuFullReductionService;
import xyz.klenkiven.kmall.coupon.service.SkuLadderService;


@Service("skuFullReductionService")
@RequiredArgsConstructor
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    private final SkuLadderService skuLadderService;
    private final MemberPriceService memberPriceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional()
    public void saveAllReduction(SkuReductionTO skuReductionTO) {
        // 6.4 Save SKU full reduction[RPC]:    sms => sms_sku_full_reduction
        SkuFullReductionEntity skuFullReduction = new SkuFullReductionEntity();
        BeanUtils.copyProperties(skuReductionTO, skuFullReduction);
        skuFullReduction.setAddOther(skuReductionTO.getPriceStatus());
        if (skuFullReduction.getFullPrice().compareTo(BigDecimal.ZERO) > 0) {
            this.save(skuFullReduction);
        }

        // 6.5 Save SKU ladder[RPC]:            sms => sms_sku_ladder
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        BeanUtils.copyProperties(skuReductionTO, skuLadderEntity);
        skuLadderEntity.setAddOther(skuReductionTO.getCountStatus());
        if (skuLadderEntity.getFullCount() > 0) {
            skuLadderService.save(skuLadderEntity);
        }

        // 6.6 Save SKU member price[RPC]:      sms => sms_member_price
        List<MemberPrice> memberPriceList = skuReductionTO.getMemberPrice();
        if (memberPriceList == null || memberPriceList.size() == 0) return;
        memberPriceList.forEach((memberPrice) -> {
            MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
            memberPriceEntity.setSkuId(skuReductionTO.getSkuId());
            memberPriceEntity.setMemberLevelId((long) memberPrice.getId());
            memberPriceEntity.setMemberLevelName(memberPrice.getName());
            memberPriceEntity.setMemberPrice(memberPrice.getPrice());
            memberPriceEntity.setAddOther(1);
            if (memberPrice.getPrice().compareTo(BigDecimal.ZERO) > 0) {
                memberPriceService.save(memberPriceEntity);
            }
        });
    }

}