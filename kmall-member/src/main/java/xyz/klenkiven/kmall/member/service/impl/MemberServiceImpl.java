package xyz.klenkiven.kmall.member.service.impl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.klenkiven.kmall.common.utils.PageUtils;
import xyz.klenkiven.kmall.common.utils.Query;

import xyz.klenkiven.kmall.member.dao.MemberDao;
import xyz.klenkiven.kmall.member.entity.MemberEntity;
import xyz.klenkiven.kmall.member.exception.MobileExistException;
import xyz.klenkiven.kmall.member.exception.UsernameExistException;
import xyz.klenkiven.kmall.member.service.MemberService;
import xyz.klenkiven.kmall.member.vo.MemberLoginVO;
import xyz.klenkiven.kmall.member.vo.RegFeignVO;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void register(RegFeignVO vo) throws MobileExistException {
        MemberEntity member = new MemberEntity();

        // Set member's level
        Long defaultLevel = baseMapper.getDefaultLevel();
        member.setLevelId(defaultLevel);

        // Check and Save Info
        checkMobileUnique(vo.getPhone());
        checkUsernameUnique(vo.getUsername());
        member.setMobile(vo.getPhone());
        member.setUsername(vo.getUsername());

        // Save Password By BCrypt
        PasswordEncoder bCrypt = new BCryptPasswordEncoder();
        member.setPassword(bCrypt.encode(vo.getPassword()));


        // Others
        member.setCreateTime(new Date());

        baseMapper.insert(member);
    }

    @Override
    public MemberEntity login(MemberLoginVO vo) {
        MemberEntity member = baseMapper.selectUserByAccount(vo.getAccount());
        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
        // Authorization
        if (member != null && bCrypt.matches(vo.getPassword(), member.getPassword())) {
            return member;
        }
        return null;
    }

    private void checkUsernameUnique(String username) {
        if (baseMapper.checkUnique("username", username) > 0) {
            throw new UsernameExistException();
        }
    }

    private void checkMobileUnique(String phone) {
        if (baseMapper.checkUnique("mobile", phone) > 0) {
            throw new MobileExistException();
        }
    }

}