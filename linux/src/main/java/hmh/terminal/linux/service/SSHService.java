package hmh.terminal.linux.service;

import com.baomidou.mybatisplus.extension.service.IService;
import hmh.terminal.linux.dao.entity.UserServer;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/1/16 21:43
 */
@Service
public interface SSHService extends IService<UserServer> {
    //如果是用户，一人一Linux号：根据uid获得ip和端口号，sid为空。
    //如果是管理员 根据sid和uid获得ip和端口。超级管理员，一人多root号
    Map<String,Object> getUserAndServer(String username, Integer sid);

    boolean checkCommand(String command);

    boolean checkToken(String username, String ipAddress,String token);
}
