package xyz.klenkiven.kmall.ware.dao;

import xyz.klenkiven.kmall.ware.entity.PurchaseEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 采购信息
 * 
 * @author klenkiven
 * @email wzl709@outlook.com
 * @date 2021-10-06 19:38:05
 */
@Mapper
public interface PurchaseDao extends BaseMapper<PurchaseEntity> {
	
}
