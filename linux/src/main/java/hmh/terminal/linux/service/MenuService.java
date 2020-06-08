package hmh.terminal.linux.service;

import com.baomidou.mybatisplus.extension.service.IService;
import hmh.terminal.linux.dao.entity.Menu;
import hmh.terminal.linux.dto.MenuDTO;
import hmh.terminal.linux.vo.MenuDataVo;
import hmh.terminal.linux.vo.MenuVo;

import java.util.List;
import java.util.Map;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/3/17 15:39
 */
public interface MenuService extends IService<Menu> {
    /**
     * 查所有的前端菜单（编辑使用）
     */
    List<MenuVo> getAllMenuList();

    /**
     * 根据登录账号，获得前端展现的菜单
     * 控制前端菜单的权限
     * @param username
     * @return
     */
    List<Menu> getMenusByUserName(String username);
    List<Menu> getMenusByUserNameOld(String username);

    /*
    * 查询菜单数据
    * */
    MenuDataVo getMenuDataByID(Integer id);

    /*
    * 将数据库获取到的数据变成前端能用的tree
    * */
    List<Map<String,Object>> buildMenusByUsername(String username);
    List<MenuVo> buildMenusLabel();

    /*
    * 获取max(id)
    * */
    int GetMaxMenuID();

    /*
    * 更新菜单
    * */
    boolean UpdateMenusLabel(List<MenuVo> menuVos);

    boolean UpdateMenusData(Menu menu);
}
