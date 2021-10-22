package xyz.klenkiven.kmall.ware.dao;

import org.apache.ibatis.annotations.Param;
import xyz.klenkiven.kmall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品库存
 * 
 * @author klenkiven
 * @email wzl709@outlook.com
 * @date 2021-10-06 19:38:05
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    /**
     * Add Sku Stock DAO
     */
    void addStock(@Param("wareId") Long wareId, @Param("skuId") Long skuId, @Param("skuNum") Integer skuNum);

    /**
     * Get Sku Stock
     */
    long getStockBySkuId(@Param("skuId") Long skuId);
}
