package xyz.klenkiven.kmall.member.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import xyz.klenkiven.kmall.common.utils.PageUtils;
import xyz.klenkiven.kmall.common.utils.Query;

import xyz.klenkiven.kmall.member.dao.MemberDao;
import xyz.klenkiven.kmall.member.entity.MemberEntity;
import xyz.klenkiven.kmall.member.exception.MobileExistException;
import xyz.klenkiven.kmall.member.exception.UsernameExistException;
import xyz.klenkiven.kmall.member.service.MemberService;
import xyz.klenkiven.kmall.member.vo.MemberLoginVO;
import xyz.klenkiven.kmall.member.vo.QQAccessToken;
import xyz.klenkiven.kmall.member.vo.RegFeignVO;
import xyz.klenkiven.kmall.member.vo.UserInfoDTO;


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
        member.setNickname(vo.getUsername());

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

    @Override
    public MemberEntity login(QQAccessToken token) {
        // Get Open ID and Client Id
        String ids = getClientAndOpenId(token.getAccess_token());
        JSONObject jsonObject = JSON.parseObject(ids);
        String openId = jsonObject.getString("openid");
        String clientId = jsonObject.getString("client_id");

        if (StringUtils.isEmpty(openId)) return null;
        // Search for member in database
        MemberEntity member = baseMapper.selectOne(new QueryWrapper<MemberEntity>().eq("social_uid", openId));
        if (member == null) {
            // If member is not here, register for new member
            MemberEntity register = new MemberEntity();
            UserInfoDTO userInfoDTO = getUserInfo(token.getAccess_token(), clientId, openId);
            if (userInfoDTO == null) return null;
            // Register for user
            register.setUsername(userInfoDTO.getNickname());
            register.setGender("男".equals(userInfoDTO.getGender())?1:0);
            register.setSocialUid(openId);
            register.setAccessToken(token.getAccess_token());
            register.setExpiresIn(token.getExpires_in());
            register.setCreateTime(new Date());
            save(register);
            return register;
        } else {
            // If user is exist, update access token
            member.setAccessToken(token.getAccess_token());
            member.setExpiresIn(token.getExpires_in());
            updateById(member);
            return member;
        }
    }

    /**
     * 获取登录用户在QQ空间的信息，包括昵称、头像、性别
     *
     * @param accessToken Access Token
     * @param clientId App Id
     * @param openId User Unique Id in this App
     * @return User info
     */
    private UserInfoDTO getUserInfo(String accessToken, String clientId, String openId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://graph.qq.com/user/get_user_info")
                .queryParam("access_token", accessToken)
                .queryParam("oauth_consumer_key", clientId)
                .queryParam("openid", openId);
        ResponseEntity<UserInfoDTO> exchange = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                UserInfoDTO.class
        );
        if (exchange.getStatusCode() != HttpStatus.OK || exchange.getBody() == null) {
            return null;
        }
        return exchange.getBody();
    }

    /**
     * Get Open Id and Client Id in this Web application
     * <p><b>Note:</b> openid是此网站上唯一对应用户身份的标识，网站可将此ID进行存储便于用户下次登录时辨识其身份</p>
     * @param access_token user token
     * @return open_id
     */
    private String getClientAndOpenId(String access_token) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://graph.qq.com/oauth2.0/me")
                .queryParam("access_token", access_token)
                .queryParam("fmt", "json");
        ResponseEntity<String> exchange = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );
        if (exchange.getStatusCode() != HttpStatus.OK || exchange.getBody() == null) {
            return "";
        }
        return exchange.getBody();
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