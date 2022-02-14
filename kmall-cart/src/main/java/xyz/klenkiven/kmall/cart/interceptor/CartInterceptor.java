package xyz.klenkiven.kmall.cart.interceptor;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import xyz.klenkiven.kmall.cart.vo.UserInfoVO;
import xyz.klenkiven.kmall.common.constant.AuthConstant;
import xyz.klenkiven.kmall.common.constant.CartConstant;
import xyz.klenkiven.kmall.common.to.UserLoginTO;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * Cart Interceptor - User Identity's Identification
 * @author klenkiven
 */
public class CartInterceptor implements HandlerInterceptor {

    public static ThreadLocal<UserInfoVO> threadLocal = new ThreadLocal<>();

    /**
     * Get /cart.html PreHandle
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        UserInfoVO userInfo = new UserInfoVO();

        HttpSession session = request.getSession();
        UserLoginTO userLoginTO = (UserLoginTO) session.getAttribute(AuthConstant.LOGIN_USER);
        // User is Login
        if (userLoginTO != null) {
            userInfo.setUserId(userLoginTO.getId());
        }

        // Get User Key from cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                if (name.equals(CartConstant.TEMP_USER_KEY_NAME)) {
                    userInfo.setUserKey(cookie.getValue());
                    userInfo.setTempUser(true);
                }
            }
        }

        // If user-key is empty, get one
        if (StringUtils.isEmpty(userInfo.getUserKey())) {
            userInfo.setUserKey(UUID.randomUUID().toString());
        }

        // Add to ThreadLocal
        threadLocal.set(userInfo);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
        UserInfoVO userInfo = threadLocal.get();

        // Has Temp User in Browser
        if (!userInfo.getTempUser()) {
            Cookie cookie = new Cookie(CartConstant.TEMP_USER_KEY_NAME, userInfo.getUserKey());
            cookie.setDomain("kmall.com");
            cookie.setMaxAge(CartConstant.TEMP_USER_KEY_AGE);
            response.addCookie(cookie);
        }
    }
}
