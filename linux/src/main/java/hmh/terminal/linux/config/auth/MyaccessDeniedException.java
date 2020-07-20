package hmh.terminal.linux.config.auth;

import org.springframework.security.access.AccessDeniedException;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/3/18 21:56
 */
public class MyaccessDeniedException extends AccessDeniedException {
    public MyaccessDeniedException(String msg) {
        super(msg);
    }
}
