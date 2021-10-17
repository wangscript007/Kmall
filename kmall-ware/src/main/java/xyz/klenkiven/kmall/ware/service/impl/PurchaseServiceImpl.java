package xyz.klenkiven.kmall.ware.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.klenkiven.kmall.common.utils.PageUtils;
import xyz.klenkiven.kmall.common.utils.Query;

import xyz.klenkiven.kmall.ware.dao.PurchaseDao;
import xyz.klenkiven.kmall.ware.entity.PurchaseEntity;
import xyz.klenkiven.kmall.ware.service.PurchaseService;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
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

}