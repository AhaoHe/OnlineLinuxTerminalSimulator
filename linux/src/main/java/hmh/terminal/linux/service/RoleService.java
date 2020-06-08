package hmh.terminal.linux.service;

import com.baomidou.mybatisplus.extension.service.IService;
import hmh.terminal.linux.dao.entity.Role;
import hmh.terminal.linux.dto.RoleFilterDTO;

import java.util.List;
import java.util.Map;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/3/17 16:27
 */
public interface RoleService extends IService<Role> {
    /**
     * 根据用户名称查询角色
     * @param userName
     * @return
     */
    List<String> getRolesByUserName(String userName);

    Map<String,Object> getPermissionByUserName(String userName);

    /*
    * 获取角色过滤器
    * */
    List<RoleFilterDTO> getRolesFilter();

}
