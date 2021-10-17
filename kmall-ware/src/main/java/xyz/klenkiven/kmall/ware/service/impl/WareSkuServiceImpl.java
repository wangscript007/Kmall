package xyz.klenkiven.kmall.ware.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.klenkiven.kmall.common.utils.PageUtils;
import xyz.klenkiven.kmall.common.utils.Query;

import xyz.klenkiven.kmall.ware.dao.WareSkuDao;
import xyz.klenkiven.kmall.ware.entity.WareSkuEntity;
import xyz.klenkiven.kmall.ware.service.WareSkuService;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        QueryWrapper<WareSkuEntity> wareSkuEntityQueryWrapper = new QueryWrapper<>();

        String wareId = (String) params.get("wareId");
        wareSkuEntityQueryWrapper.eq(!StringUtils.isEmpty(wareId), "ware_id", wareId);
        String skuId = (String) params.get("skuId");
        wareSkuEntityQueryWrapper.eq(!StringUtils.isEmpty(skuId), "sku_id", skuId);

        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                wareSkuEntityQueryWrapper
        );

        return new PageUtils(page);
    }

}