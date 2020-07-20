package hmh.terminal.linux.service;

import com.baomidou.mybatisplus.extension.service.IService;
import hmh.terminal.linux.dao.entity.RoleMenu;

import java.util.List;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/5/24 15:26
 */
public interface RoleMenuService extends IService<RoleMenu> {

    int DelRoleMenu(List<Integer> info);

    boolean SaveRoleMenu(Integer id,int[] menuIds);
}
