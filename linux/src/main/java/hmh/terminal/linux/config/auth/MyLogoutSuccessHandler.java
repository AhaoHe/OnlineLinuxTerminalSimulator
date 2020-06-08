package hmh.terminal.linux.config.auth;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/3/19 16:53
 */

import hmh.terminal.linux.result.JsonResult;
import hmh.terminal.linux.result.ResultFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 成功退出处理器
 */
@Component
@Slf4j
public class MyLogoutSuccessHandler extends JSONAuthentication implements LogoutSuccessHandler {
    private String header = "Authorization";
    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {
        UserDetails user = (UserDetails) authentication.getPrincipal();
        String username = user.getUsername();
        System.out.println(username+"退出成功");
        log.info(username+"退出成功");
        /*String headerToken = request.getHeader(header);
        String token = headerToken.replace("Bearer", "").trim();
        String username = jwtTokenUtil.getUsernameFromToken(token);
        log.info(username+"退出成功");*/
        JsonResult data = ResultFactory.SuccessResult("退出成功");
        super.WriteJSON(request,response,data);
    }
}