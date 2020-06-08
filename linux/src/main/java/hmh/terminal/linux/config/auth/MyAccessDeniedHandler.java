package hmh.terminal.linux.config.auth;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/3/19 16:47
 */

import hmh.terminal.linux.result.JsonResult;
import hmh.terminal.linux.result.ResultFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限校验处理器
 */
@Component
public class MyAccessDeniedHandler extends JSONAuthentication implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        //装入token
        JsonResult data = ResultFactory.FailResult("权限不足:"+accessDeniedException.getMessage());
        //输出
        this.WriteJSON(request, response, data);
    }
}