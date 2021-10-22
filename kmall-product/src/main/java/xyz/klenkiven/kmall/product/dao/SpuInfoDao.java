package xyz.klenkiven.kmall.product.dao;

import org.apache.ibatis.annotations.Param;
import xyz.klenkiven.kmall.product.entity.SpuInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * spu信息
 * 
 * @author klenkiven
 * @email wzl709@outlook.com
 * @date 2021-08-29 16:07:27
 */
@Mapper
public interface SpuInfoDao extends BaseMapper<SpuInfoEntity> {

    /**
     * Update SPU Status
     */
    void updateSpuStatus(@Param("spuId") Long spuId, @Param("code") Integer code);

}
