package hmh.terminal.linux.dao.mapper.user_server;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import hmh.terminal.linux.dao.entity.UserServer;
import hmh.terminal.linux.dto.DiskDTO;
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
public interface UserServerMapper extends BaseMapper<UserServer> {
    //根据uid获得ip和端口号,sid为空。用户一人一Linux号
    //如果是管理员 根据sid和uid获得ip和端口。超级管理员，一人多root号
    //排除被禁的用户
    Map<String,Object> getUserServerByUserName(@Param("username") String username, @Param("sid") Integer sid);

    //所有，不管禁没禁,但是排除删了的
    Map<String,Object> getUserServerAll(@Param("username") String username, @Param("sid") Integer sid);

    int BanUser(@Param("username")String username);

    List<String> getGroupServerUserName(@Param("groupId")Integer groupId);
    List<String> getGroupServerAdminUserName(@Param("groupId")Integer groupId);

    IPage<List<DiskDTO>> disk(IPage<DiskDTO> page, @Param("search") String search,@Param("level") Integer level,@Param("groupId") Integer groupId);
}
