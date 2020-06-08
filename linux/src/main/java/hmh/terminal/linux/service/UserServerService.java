package hmh.terminal.linux.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import hmh.terminal.linux.dao.entity.UserServer;
import hmh.terminal.linux.dto.DiskDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/6/3 23:31
 */
public interface UserServerService extends IService<UserServer> {

    int BanUser(String username);

    IPage<List<DiskDTO>> disk(Integer current, Integer size, String search,Integer level,Integer groupId);

    List<String> diskUser(Integer uid,String username);

    boolean updateDisk(Integer uid, String username,String disk);

}
