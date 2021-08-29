package xyz.klenkiven.kmall.member.dao;

import xyz.klenkiven.kmall.member.entity.MemberLevelEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员等级
 * 
 * @author klenkiven
 * @email wzl709@outlook.com
 * @date 2021-08-29 20:56:00
 */
@Mapper
public interface MemberLevelDao extends BaseMapper<MemberLevelEntity> {
	
}
