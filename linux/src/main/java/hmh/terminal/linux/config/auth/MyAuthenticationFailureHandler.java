package hmh.terminal.linux.config.auth;

import hmh.terminal.linux.result.JsonResult;
import hmh.terminal.linux.result.ResultFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/3/19 13:49
 */
@Component
public class MyAuthenticationFailureHandler extends JSONAuthentication implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        JsonResult data = ResultFactory.FailResult("登录失败："+e.getMessage());
        //输出
        this.WriteJSON(request,response,data);
    }
}
