package xyz.klenkiven.kmall.ware.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.klenkiven.kmall.common.utils.PageUtils;
import xyz.klenkiven.kmall.common.utils.Query;

import xyz.klenkiven.kmall.ware.dao.PurchaseDetailDao;
import xyz.klenkiven.kmall.ware.entity.PurchaseDetailEntity;
import xyz.klenkiven.kmall.ware.service.PurchaseDetailService;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<PurchaseDetailEntity> purchaseDetailEntityQueryWrapper = new QueryWrapper<>();

        String wareId = (String) params.get("wareId");
        purchaseDetailEntityQueryWrapper.eq(!StringUtils.isBlank(wareId), "ware_id", wareId);

        String status = (String) params.get("status");
        purchaseDetailEntityQueryWrapper.eq(!StringUtils.isBlank(status), "status", status);

        String key = (String) params.get("key");
        purchaseDetailEntityQueryWrapper.and(!StringUtils.isBlank(key), wrapper ->
                wrapper.eq("purchase_id", key).or().eq("sku_id", key));

        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                purchaseDetailEntityQueryWrapper
        );

        return new PageUtils(page);
    }

}