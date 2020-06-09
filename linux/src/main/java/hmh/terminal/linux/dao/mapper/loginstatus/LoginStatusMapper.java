package hmh.terminal.linux.dao.mapper.loginstatus;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import hmh.terminal.linux.dao.entity.LoginStatus;
import hmh.terminal.linux.dto.UserOnlineDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/6/1 21:49
 */
@Mapper
public interface LoginStatusMapper extends BaseMapper<LoginStatus> {

    IPage<List<UserOnlineDTO>> getListOnline(IPage<UserOnlineDTO> page,@Param("users")String users,@Param("map")Map<String,Object>  map,@Param("status") Integer status);

    List<Map<String,String>> getChartData(@Param("startTime")String startTime, @Param("endTime")String endTime,@Param("status")int status);
    List<Map<String,String>> getUserData(@Param("startTime")String startTime, @Param("endTime")String endTime,@Param("username")String username);
}
