package xyz.klenkiven.kmall.member.dao;

import org.apache.ibatis.annotations.Param;
import xyz.klenkiven.kmall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author klenkiven
 * @email wzl709@outlook.com
 * @date 2021-08-29 20:56:00
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {

    /**
     * Check column is Unique
     * @param column column name
     * @param value value
     * @return is unique
     */
    int checkUnique(@Param("col") String column, @Param("val") String value);

    /**
     * Get User's Default Level
     */
    Long getDefaultLevel();
}
