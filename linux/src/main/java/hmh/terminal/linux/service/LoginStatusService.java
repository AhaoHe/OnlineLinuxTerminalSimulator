package hmh.terminal.linux.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import hmh.terminal.linux.dao.entity.LoginStatus;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/6/1 21:59
 */
public interface LoginStatusService extends IService<LoginStatus> {

    Map<String,List> getChartData(String startTime, int days);

    List<String> getDFH(String username);

}
