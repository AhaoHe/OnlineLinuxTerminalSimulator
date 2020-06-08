package hmh.terminal.linux.dao.mapper.groupcommand;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import hmh.terminal.linux.dao.entity.Group;
import hmh.terminal.linux.dao.entity.GroupCommand;
import hmh.terminal.linux.vo.GroupVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/3/18 1:32
 */
@Mapper
public interface GroupCommandMapper extends BaseMapper<GroupCommand> {
    int delUnchecked(@Param("groupId")Integer groupId,@Param("ids")String ids);
}
