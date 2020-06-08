package hmh.terminal.linux.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hmh.terminal.linux.dao.entity.Command;
import hmh.terminal.linux.dao.entity.UserServer;
import hmh.terminal.linux.dao.mapper.command.CommandMapper;
import hmh.terminal.linux.dao.mapper.user.UserMapper;
import hmh.terminal.linux.dao.mapper.user_server.UserServerMapper;
import hmh.terminal.linux.service.SSHService;
import hmh.terminal.linux.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/1/16 21:39
 */
@Service("SSHService")
@Slf4j
public class SSHServiceImpl extends ServiceImpl<UserServerMapper, UserServer> implements SSHService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    CommandMapper commandMapper;

    @Override
    public Map<String,Object> getUserAndServer(String username,Integer sid) {
        Map<String,Object> uss = this.baseMapper.getUserServerByUserName(username,sid);
        if (uss!=null) {

            //加密解密
            return uss;
        }else {
            //若有多个，则说明为管理员且没给serverId
            log.debug("管理员访问，数据缺少sid，需要查看业务逻辑");
           return null;
        }
    }

    @Override
    public boolean checkCommand(String command) {

        return true;
    }

    @Override
    public boolean checkToken(String username, String ipAddress, String token){
        Map<String,String> map = userMapper.checkToken(username);
        return map.get("ipaddress").equals(ipAddress) && map.get("token").equals(token);
    }

}
