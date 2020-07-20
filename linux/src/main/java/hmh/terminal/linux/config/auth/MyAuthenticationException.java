package hmh.terminal.linux.config.auth;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/3/18 21:54
 */

import org.springframework.security.core.AuthenticationException;

/**
 * 自定义异常类，继承AuthenticationException
 * 在有throws AuthenticationException方法上捕获
 * 方式：throw new  MyAuthenticationException
 */
public class MyAuthenticationException  extends AuthenticationException {

    public MyAuthenticationException(String msg) {
        super(msg);
    }
}
