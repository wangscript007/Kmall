package xyz.klenkiven.kmall.ware.dao;

import xyz.klenkiven.kmall.ware.entity.WmsWareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品库存
 * 
 * @author klenkiven
 * @email wzl709@outlook.com
 * @date 2021-08-29 21:12:37
 */
@Mapper
public interface WmsWareSkuDao extends BaseMapper<WmsWareSkuEntity> {
	
}
