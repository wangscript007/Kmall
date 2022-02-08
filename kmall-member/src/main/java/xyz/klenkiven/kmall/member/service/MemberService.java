package xyz.klenkiven.kmall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.klenkiven.kmall.common.utils.PageUtils;
import xyz.klenkiven.kmall.member.entity.MemberEntity;
import xyz.klenkiven.kmall.member.vo.MemberLoginVO;
import xyz.klenkiven.kmall.member.vo.RegFeignVO;

import java.util.Map;

/**
 * 会员
 *
 * @author klenkiven
 * @email wzl709@outlook.com
 * @date 2021-08-29 20:56:00
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * Register Member
     * @param vo member info
     */
    void register(RegFeignVO vo);

    /**
     * Check for members' info valid
     */
    MemberEntity login(MemberLoginVO vo);
}

