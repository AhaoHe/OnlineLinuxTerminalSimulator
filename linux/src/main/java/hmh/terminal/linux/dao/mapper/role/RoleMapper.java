package hmh.terminal.linux.dao.mapper.role;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import hmh.terminal.linux.dao.entity.Role;
import hmh.terminal.linux.dto.RoleFilterDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/1/12 1:47
 */
public interface RoleMapper extends BaseMapper<Role> {

    /*
    * 根据用户名查找角色
    * */
    List<String> getRolesByUserName(@Param("userName") String userName);


    Map<String,Object> getPermissionByUserName(@Param("userName") String userName);

    /*
    * 获取role过滤器
    * */
    List<RoleFilterDTO> getRoleFilter();

}
