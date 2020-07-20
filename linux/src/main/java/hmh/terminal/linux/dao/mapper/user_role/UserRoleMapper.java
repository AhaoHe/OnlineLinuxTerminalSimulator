package hmh.terminal.linux.dao.mapper.user_role;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import hmh.terminal.linux.dao.entity.UserRole;

import java.util.Map;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/3/17 16:21
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {
    //
    Map<String,String> getServerRegister();
}
