package hmh.terminal.linux.dao.mapper.menu;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import hmh.terminal.linux.dao.entity.Menu;
import hmh.terminal.linux.dto.MenuDTO;
import hmh.terminal.linux.vo.MenuDataVo;
import hmh.terminal.linux.vo.MenuVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/1/12 1:47
 */
public interface MenuMapper extends BaseMapper<Menu> {
    /**
     * 查所有的前端菜单（编辑使用）
     */
    List<MenuVo> getAllMenuList();
    List<MenuVo> getLabelMenuList();

    /**
     * 根据登录账号，获得前端展现的菜单
     * 控制前端菜单的权限
     * @param username
     * @return
     */
    List<Menu> getMenusByUserName(@Param("username") String username);
    List<Menu> getMenusByUserNameOld(@Param("username") String username);

    /*
    * 查询最大id
    * */
    //@Select("SELECT Auto_increment FROM information_schema.tables WHERE Table_Schema='linuxterminal' AND table_name = 'menu';")
    @Select("SELECT max(id)+1 FROM menu")
    int getMaxMenuID();

    /*
    * 查询菜单id的数据
    * */
    MenuDataVo getMenuData(@Param("id")Integer id);
}
