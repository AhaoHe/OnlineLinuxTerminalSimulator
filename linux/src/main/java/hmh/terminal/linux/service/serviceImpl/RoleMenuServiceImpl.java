package hmh.terminal.linux.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hmh.terminal.linux.dao.entity.Api;
import hmh.terminal.linux.dao.entity.RoleMenu;
import hmh.terminal.linux.dao.mapper.api.ApiMapper;
import hmh.terminal.linux.dao.mapper.role_menu.RoleMenuMapper;
import hmh.terminal.linux.service.ApiService;
import hmh.terminal.linux.service.RoleMenuService;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/5/24 15:27
 */
@Service("RoleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

    @Override
    public int DelRoleMenu(List<Integer> info){
        LambdaUpdateWrapper<RoleMenu> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(RoleMenu::getMenuId,info);
        return this.baseMapper.delete(updateWrapper);
    }

    @Override
    public boolean SaveRoleMenu(Integer id,int[] menuIds) {
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId,id);
        this.baseMapper.delete(queryWrapper);
        List<RoleMenu> roleMenus = new LinkedList<>();
        for (Integer menuId : menuIds) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(id);
            roleMenu.setMenuId(menuId);
            roleMenus.add(roleMenu);
        }
        return this.saveBatch(roleMenus);
    }

}
