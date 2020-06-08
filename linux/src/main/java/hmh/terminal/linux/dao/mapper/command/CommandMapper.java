package hmh.terminal.linux.dao.mapper.command;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import hmh.terminal.linux.dao.entity.Api;
import hmh.terminal.linux.dao.entity.Command;
import hmh.terminal.linux.dao.provider.ApiProvider;
import hmh.terminal.linux.vo.ApiVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/3/18 1:32
 */
public interface CommandMapper extends BaseMapper<Command> {


    IPage<List<Command>> getCommandInfo(IPage<Command> page,@Param("search")String search);

    int changeStatus(@Param("ids")String ids,@Param("status")Integer status);

    List<Integer> getCommandIdByGroup(@Param("groupId")Integer groupId);
}
