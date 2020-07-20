package hmh.terminal.linux.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import hmh.terminal.linux.dao.entity.Server;

import java.util.List;
import java.util.Map;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/5/20 20:50
 */
public interface ServerService extends IService<Server> {

    List<Server> getAllServer();

    List<String> serverList();

    boolean updateServerInfo(Server server);

    boolean saveServerInfo(Server server);
//获取服务器下的用户
    IPage<List<Map<String,Object>>> getSeverUsers(String query);
//获取选择器
    List<String> getServerSelection(Integer serverId);
    //转移到新服务器
    boolean toNewServer(String users,Integer serverIdOld,Integer serverIdNew);
}
