package hmh.terminal.linux.config.auth;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/3/19 16:53
 */

import hmh.terminal.linux.service.auth.AuthUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 退出Handler
 */
@Component
public class MyLogoutHandler extends JSONAuthentication implements LogoutHandler {
    private String header = "Authorization";
    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {

        //这里需要注意，如果将SpringSecurity的session配置为无状态，或者不保存session，这里authentication为null！！
        //原因：LogoutFilter远远比UsernamePasswordAuthenticationFilter先执行了
        //解决办法：在配置中 logouFiter.class之前加个MyOncePerRequestFilter
        String headerToken = request.getHeader(header);
        System.out.println("logout header Token = " + headerToken);
        System.out.println("logout request getMethod = " + request.getMethod());
        //
        if (!StringUtils.isEmpty(headerToken)) {
            //postMan测试时，自动假如的前缀，要去掉。
            //String token = headerToken.replace("Bearer", "").trim();
            System.out.println("authentication = " + authentication);
            SecurityContextHolder.clearContext();
        }
    }
}
