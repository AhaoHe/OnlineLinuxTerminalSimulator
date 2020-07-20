package hmh.terminal.linux.config.auth;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/3/19 16:45
 */

import hmh.terminal.linux.result.JsonResult;
import hmh.terminal.linux.result.ResultFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 身份校验失败处理器，如 token 错误
 */
@Component
public class MyAuthenticationEntryPoint extends JSONAuthentication  implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        JsonResult data = ResultFactory.UnAuthorizedResult("访问此资源需要完全身份验证（"+authException.getMessage()+"）！");
        //输出
        this.WriteJSON(request, response, data);
    }
}