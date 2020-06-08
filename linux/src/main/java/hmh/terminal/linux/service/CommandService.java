package hmh.terminal.linux.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import hmh.terminal.linux.dao.entity.Command;
import hmh.terminal.linux.vo.CommandVo;

import java.util.List;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/5/5 20:34
 */
public interface CommandService extends IService<Command> {
    List<Command> findAllLikeCommand(String command);

    IPage<List<Command>> getInfo(CommandVo commandVo);

    int changeStatus(String ids,Integer status);

    List<Integer> getGroupCommand(Integer groupId);

    boolean groupCommand(Integer groupId,String selection,String unselection);

}
