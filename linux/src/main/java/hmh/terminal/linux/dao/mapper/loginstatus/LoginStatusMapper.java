package hmh.terminal.linux.dao.mapper.loginstatus;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import hmh.terminal.linux.dao.entity.LoginStatus;
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

    List<Map<String,String>> getChartData(@Param("startTime")String startTime, @Param("endTime")String endTime,@Param("status")int status);
}
