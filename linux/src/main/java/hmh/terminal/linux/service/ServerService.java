package hmh.terminal.linux.service;

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
}
