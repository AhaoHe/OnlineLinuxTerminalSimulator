package hmh.terminal.linux.utils;

import hmh.terminal.linux.config.auth.MyaccessDeniedException;
import hmh.terminal.linux.dao.entity.Api;
import hmh.terminal.linux.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/3/18 21:10
 * 动态权限
 */
@Component
public class DynamicPermission {
    @Autowired
    ApiService service;

    /**
     * 判断有访问API的权限
     * @param request
     * @param authentication
     * @return
     * @throws MyaccessDeniedException
     */
    public boolean checkPermisstion(HttpServletRequest request,
                                    Authentication authentication) throws MyaccessDeniedException {
        Object principal = authentication.getPrincipal();
        System.out.println("DynamicPermission principal = " + principal);

        if(principal instanceof UserDetails) {

            UserDetails userDetails = (UserDetails) principal;
            //得到当前的账号
            String username = userDetails.getUsername();
            //Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();

            // System.out.println("DynamicPermission  username = " + username);
            //通过账号获取资源鉴权
            //List<Api> apiUrls = service.getApiUrlByUserName(username);
            List<Api> apiUrls = service.getApiUrlByUserNameNew(username);

            AntPathMatcher antPathMatcher = new AntPathMatcher();
            //当前访问路径
            String requestURI = request.getRequestURI();
            //提交类型
            String urlMethod = request.getMethod();

            // System.out.println("DynamicPermission requestURI = " + requestURI);

            //判断当前路径中是否在资源鉴权中
            boolean rs = apiUrls.stream().anyMatch(item->{
                //判断URL是否匹配
                boolean hashAntPath = antPathMatcher.match(item.getApiUrl(),requestURI);

                //判断请求方式是否和数据库中匹配（数据库存储：GET,POST,PUT,DELETE）
                String dbMethod = item.getApiMethod();

                //处理null，万一数据库存值
                dbMethod = (dbMethod == null )? "": dbMethod;
                int hasMethod   = dbMethod.indexOf(urlMethod);

                System.out.println("hashAntPath = " + hashAntPath);
                System.out.println("hasMethod = " + hasMethod);
                System.out.println("hashAntPath && hasMethod = " + (hashAntPath && hasMethod !=-1));
                //两者都成立，返回真，否则返回假
                return hashAntPath && (hasMethod !=-1);
            });
            //返回
            if (rs) {
                return rs;
            }else {
                throw  new MyaccessDeniedException("您没有访问该API的权限！");
            }

        }else{
            throw  new MyaccessDeniedException("不是UserDetails类型！");
        }
    }


}
