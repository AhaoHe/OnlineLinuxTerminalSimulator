package hmh.terminal.linux.dao.mapper.server;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import hmh.terminal.linux.dao.entity.Server;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/1/12 1:47
 */
@Mapper
public interface ServerMapper extends BaseMapper<Server> {
    Map<String,String> getServerByServerId(@Param("id") int id);
    Map<String,Object> getServerByServerIdAndStatus(@Param("id") int id,@Param("status") int status);
    Map<String,Object> getServerRegister();
    List<String> severList();
}
