package xyz.klenkiven.kmall.member.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import xyz.klenkiven.kmall.common.exception.ExceptionCodeEnum;
import xyz.klenkiven.kmall.common.utils.Result;
import xyz.klenkiven.kmall.member.entity.MemberEntity;
import xyz.klenkiven.kmall.member.exception.MobileExistException;
import xyz.klenkiven.kmall.member.exception.UsernameExistException;
import xyz.klenkiven.kmall.member.feign.CouponFeignService;
import xyz.klenkiven.kmall.member.service.MemberService;
import xyz.klenkiven.kmall.common.utils.PageUtils;
import xyz.klenkiven.kmall.common.utils.R;
import xyz.klenkiven.kmall.member.vo.MemberLoginVO;
import xyz.klenkiven.kmall.member.vo.QQAccessToken;
import xyz.klenkiven.kmall.member.vo.RegFeignVO;


/**
 * 会员
 *
 * @author klenkiven
 * @email wzl709@outlook.com
 * @date 2021-08-29 20:56:00
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private CouponFeignService couponFeignService;

    @RequestMapping("/coupons")
    public R testOpenFeign() {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setNickname("KlenKiven");

        R memberCoupons = couponFeignService.memberCoupons();
        return R.ok().put("member", memberEntity).put("coupons", memberCoupons.get("coupons"));
    }

    /**
     * [FEIGN] ] Register Service
     */
    @PostMapping("register")
    public Result<?> register(@RequestBody RegFeignVO vo) {
        // Check for exist
        try {
            memberService.register(vo);
        } catch (UsernameExistException e) {
            return Result.error(ExceptionCodeEnum.USERNAME_EXIST_ERROR.getCode(),
                    ExceptionCodeEnum.USERNAME_EXIST_ERROR.getMessage());
        } catch (MobileExistException e) {
            return Result.error(ExceptionCodeEnum.PHONE_EXIST_ERROR.getCode(),
                    ExceptionCodeEnum.PHONE_EXIST_ERROR.getMessage());
        }

        return Result.ok();
    }

    /**
     * [FEIGN] Member Login
     */
    @PostMapping("/login")
    public Result<MemberEntity> login(@RequestBody MemberLoginVO vo) {
        MemberEntity entity = memberService.login(vo);
        if (entity != null) {
            return Result.ok(entity);
        } else {
            return Result.error(ExceptionCodeEnum.USERNAME_PASSWORD_INVALID.getCode(),
                    ExceptionCodeEnum.USERNAME_PASSWORD_INVALID.getMessage());
        }
    }

    /**
     * [FEIGN] Member Oauth2.0 Login
     */
    @PostMapping("/oauth2.0/login")
    public Result<MemberEntity> oauthLogin(@RequestBody QQAccessToken token) {
        MemberEntity entity = memberService.login(token);
        if (entity != null) {
            return Result.ok(entity);
        } else {
            return Result.error(ExceptionCodeEnum.USERNAME_PASSWORD_INVALID.getCode(),
                    ExceptionCodeEnum.USERNAME_PASSWORD_INVALID.getMessage());
        }
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("member:member:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("member:member:info")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("member:member:save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("member:member:update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("member:member:delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
