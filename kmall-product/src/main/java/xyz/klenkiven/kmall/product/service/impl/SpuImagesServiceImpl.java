package xyz.klenkiven.kmall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.klenkiven.kmall.common.utils.PageUtils;
import xyz.klenkiven.kmall.common.utils.Query;

import xyz.klenkiven.kmall.product.dao.SpuImagesDao;
import xyz.klenkiven.kmall.product.entity.SpuImagesEntity;
import xyz.klenkiven.kmall.product.service.SpuImagesService;


@Service("spuImagesService")
public class SpuImagesServiceImpl extends ServiceImpl<SpuImagesDao, SpuImagesEntity> implements SpuImagesService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuImagesEntity> page = this.page(
                new Query<SpuImagesEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveImages(Long id, List<String> images) {
        if (images == null || images.size() == 0) return;

        List<SpuImagesEntity> collect = images.stream()
                .map((img) -> {
                    SpuImagesEntity imagesEntity = new SpuImagesEntity();
                    imagesEntity.setSpuId(id);
                    imagesEntity.setImgUrl(img);
                    return imagesEntity;
                }).collect(Collectors.toList());
        this.saveBatch(collect);
    }

}