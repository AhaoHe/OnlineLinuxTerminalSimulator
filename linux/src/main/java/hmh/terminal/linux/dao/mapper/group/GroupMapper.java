package hmh.terminal.linux.dao.mapper.group;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import hmh.terminal.linux.dao.entity.Group;
import hmh.terminal.linux.dao.entity.RoleApi;
import hmh.terminal.linux.vo.GroupVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Map;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/3/18 1:32
 */
@Mapper
public interface GroupMapper extends BaseMapper<Group> {
    //获取注册时的组
    Map<String,Object> getGroupName();
    //获取该组下所属服务器的无组
    Integer getGroupIdDefault(@Param("groupId")Integer groupId);

    List<Map<String,Object>> getUserUnchecked(@Param("groupVo")GroupVo groupVo);
    List<Map<String,Object>> getUserChecked(@Param("groupVo")GroupVo groupVo);
    List<Map<String,Object>> getApplyUnchecked(@Param("groupId") Integer groupId,@Param("search") String search);


    List<String> getGroupUser(@Param("groupId") Integer groupId);
    List<String> getGroupNoAmdinUser(@Param("groupId") Integer groupId);

    //获取组的服务器信息，包含组名组状态等
    Map<String,Object> getGroupDefault(int groupId);
    //(用服务器id)获取服务器信息，用于连接服务器
    Map<String,Object> getGroupServer(int serverId);


    //可申请的组列表
    List<Map<String,Object>> applyGroupList(@Param("username") String username,@Param("groupId")Integer groupId);
    //添加入组申请表
    int applyGroup(@Param("groupId")Integer groupId,@Param("username") String username);
    //是否申请过
    int isOneApply(@Param("groupId")Integer groupId,@Param("username") String username);
    //删除申请
    int delApply(@Param("groupId")Integer groupId,@Param("username") String username);

}
