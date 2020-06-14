package hmh.terminal.linux.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import hmh.terminal.linux.dao.entity.LoginStatus;
import hmh.terminal.linux.dao.entity.UserCommand;
import hmh.terminal.linux.dto.UserOnlineDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/6/1 21:59
 */
public interface LoginStatusService extends IService<LoginStatus> {

    IPage<List<UserOnlineDTO>> getListNoBan(String request);
    IPage<List<UserOnlineDTO>> getListWithBan(String request);

    IPage<UserCommand> getHistoryCommand(String request);

    Map<String,List> getChartData(String startTime, int days);
    Map<String,List> getUserChartData(String startTime, int days,String username);
    Map<String,List> getCommandChartData(String startTime, int days);

    Map<String,Integer> getCount();

    List<String> getDFH(String username);

}
