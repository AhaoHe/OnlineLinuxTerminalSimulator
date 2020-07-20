package hmh.terminal.linux.dao.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import hmh.terminal.linux.dao.entity.Server;
import hmh.terminal.linux.dao.entity.User;
import hmh.terminal.linux.dto.ManageUserDTO;
import hmh.terminal.linux.vo.UserInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/1/12 1:47
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    IPage<List<ManageUserDTO>> findAllUser(IPage<ManageUserDTO> page);
    IPage<List<ManageUserDTO>> findUserBy(IPage<ManageUserDTO> page, @Param("userInfo")UserInfoVo userInfoVo);
    IPage<List<ManageUserDTO>> findUserLikeUsername(IPage<ManageUserDTO> page, @Param("username") String username);

    @Select("SELECT ipaddress,token FROM loginstatus WHERE login_time = (SELECT Max(login_time) AS login_time FROM loginstatus WHERE loginstatus=0 AND username=#{username}) AND username = #{username};")
    Map<String,String> checkToken(@Param("username") String username);

//获取组内的所有成员（无组不显示）
    IPage<List<Map<String, Object>>> getUserServerByUsername(IPage<Map<String,Object>>page,@Param("groupId") Integer groupId,@Param("level")Integer level,@Param("search")String search);
}
