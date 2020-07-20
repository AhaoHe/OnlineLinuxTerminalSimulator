package hmh.terminal.linux.config.auth;

import hmh.terminal.linux.dao.entity.LoginStatus;
import hmh.terminal.linux.dao.entity.Menu;
import hmh.terminal.linux.result.JsonResult;
import hmh.terminal.linux.result.ResultFactory;
import hmh.terminal.linux.service.LoginStatusService;
import hmh.terminal.linux.service.MenuService;
import hmh.terminal.linux.utils.TokenCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/3/19 13:29
 */
@Component
@Slf4j
public class MyAuthenticationSuccessHandler extends JSONAuthentication implements AuthenticationSuccessHandler {
    @Autowired
    MenuService service;
    @Autowired
    LoginStatusService loginStatusService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        final Integer status = 0; //平台为0，Linux系统为1

        //取得账号信息
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //
        System.out.println("userDetails = " + userDetails);
        //取token
        //好的解决方案，登录成功后token存储到数据库中
        //只要token还在过期内，不需要每次重新生成
        //先去缓存中找
        String token = TokenCache.getTokenFromCache(userDetails.getUsername());

        /*if(token != null) {//刷新token
            token = jwtTokenUtil.refreshToken(token);
            TokenCache.setToken(userDetails.getUsername(),token);
        }*/

        if(token ==null) {
            log.info(userDetails.getUsername()+"初次登录，生成新token...");
            //如果token为空，则去创建一个新的token
            //jwtTokenUtil = new JwtTokenUtil();
            token = jwtTokenUtil.generateToken(userDetails);
            //把新的token存储到缓存中
            TokenCache.setToken(userDetails.getUsername(),token);
        }
        LoginStatus loginStatus = new LoginStatus();
        loginStatus.setUsername(userDetails.getUsername());
        loginStatus.setLoginstatus(status);
        loginStatus.setToken(token);
        loginStatus.setIpAddress(((WebAuthenticationDetails) SecurityContextHolder.getContext().
                getAuthentication().getDetails()).getRemoteAddress());
        loginStatusService.save(loginStatus);
        //加载前端菜单
        //List<Menu> menus = service.getMenusByUserName(userDetails.getUsername());
        //
        Map<String,Object> map = new HashMap<>();
        map.put("username",userDetails.getUsername());
        //map.put("auth",userDetails.getAuthorities());
        //map.put("menus",menus);
        map.put("token",token);
        //装入token
        JsonResult data = ResultFactory.SuccessResult(map);
        //输出
        this.WriteJSON(request, response, data);

    }
}
