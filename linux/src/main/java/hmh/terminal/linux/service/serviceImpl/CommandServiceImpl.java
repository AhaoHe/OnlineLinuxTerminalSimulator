package hmh.terminal.linux.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hmh.terminal.linux.dao.entity.Command;
import hmh.terminal.linux.dao.entity.GroupCommand;
import hmh.terminal.linux.dao.mapper.command.CommandMapper;
import hmh.terminal.linux.dao.mapper.groupcommand.GroupCommandMapper;
import hmh.terminal.linux.dto.ManageUserDTO;
import hmh.terminal.linux.service.CommandService;
import hmh.terminal.linux.vo.CommandVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/5/5 20:34
 */
@Service
public class CommandServiceImpl extends ServiceImpl<CommandMapper,Command> implements CommandService {

    @Autowired
    GroupCommandMapper groupCommandMapper;

    @Override
    public List<Command> findAllLikeCommand(String command) {
        QueryWrapper<Command> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .select("command","description")
                .ne("status",7)
                .likeRight("command",command);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public IPage<List<Command>> getInfo(CommandVo commandVo) {
        IPage<Command> page = new Page<>(commandVo.getCurrent(),commandVo.getPageSize());
        return this.baseMapper.getCommandInfo(page,commandVo.getSearch());
    }

    @Override
    public int changeStatus(String ids, Integer status) {
        return this.baseMapper.changeStatus(ids,status);
    }

    @Override
    public List<Integer> getGroupCommand(Integer groupId) {
        return this.baseMapper.getCommandIdByGroup(groupId);
    }

    @Override
    public boolean groupCommand(Integer groupId, String selection, String unselection) {
        if(!"".equals(selection)){
            String[] checked=selection.split(",");
            for (String val:checked){
                GroupCommand groupCommand= new GroupCommand();
                groupCommand.setCommandId(Integer.parseInt(val));
                groupCommand.setGroupId(groupId);
                groupCommandMapper.insert(groupCommand);
            }
        }
        if(!"".equals(unselection)){
            groupCommandMapper.delUnchecked(groupId,unselection);
        }
        return true;
    }
}
