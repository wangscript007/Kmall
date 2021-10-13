package xyz.klenkiven.kmall.product.service.impl;

import com.mysql.cj.util.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.klenkiven.kmall.common.utils.PageUtils;
import xyz.klenkiven.kmall.common.utils.Query;

import xyz.klenkiven.kmall.product.dao.BrandDao;
import xyz.klenkiven.kmall.product.entity.BrandEntity;
import xyz.klenkiven.kmall.product.service.BrandService;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<BrandEntity> resultPage;
        IPage<BrandEntity> paramPage = new Query<BrandEntity>().getPage(params);
        QueryWrapper<BrandEntity> queryWrapper = new QueryWrapper<>();

        String key = (String) params.get("key");
        if (!StringUtils.isNullOrEmpty(key)) {
            queryWrapper.eq("brand_id", key);
            queryWrapper.or();
            queryWrapper.like("name", "%" + key + "%");
        }

        resultPage = this.page(paramPage, queryWrapper);
        return new PageUtils(resultPage);
    }

}