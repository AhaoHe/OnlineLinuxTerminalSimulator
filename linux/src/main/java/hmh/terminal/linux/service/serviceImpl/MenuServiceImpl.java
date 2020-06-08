package hmh.terminal.linux.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hmh.terminal.linux.dao.entity.Menu;
import hmh.terminal.linux.dao.mapper.menu.MenuMapper;
import hmh.terminal.linux.dto.MenuDTO;
import hmh.terminal.linux.service.MenuService;
import hmh.terminal.linux.vo.MenuDataVo;
import hmh.terminal.linux.vo.MenuVo;
import org.springframework.context.annotation.Condition;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/3/17 15:39
 */
@Service("MenuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper,Menu> implements MenuService {
    /**
     * 查所有的前端菜单（编辑使用）
     */
    @Override
    public List<MenuVo> getAllMenuList(){
        return this.baseMapper.getAllMenuList();
    }

    /**
     * 根据登录账号，获得前端展现的菜单
     * 控制前端菜单的权限
     * @param username
     * @return
     */
    @Override
    public List<Menu> getMenusByUserName(String username){
        return this.baseMapper.getMenusByUserName(username);
    }
    public List<Menu> getMenusByUserNameOld(String username){
        return this.baseMapper.getMenusByUserNameOld(username);
    }

    @Override
    public MenuDataVo getMenuDataByID(Integer id) {
        return this.baseMapper.getMenuData(id);
    }


    @Override
    public List<Map<String,Object>> buildMenusByUsername(String username) {
        List<Menu> menus =  this.baseMapper.getMenusByUserNameOld(username);
        List<Map<String,Object>> result = new LinkedList<>();
        result = buildTree(0,menus,result);
        return  result;
    }

    @Override
    public List<MenuVo> buildMenusLabel() {
        List<MenuVo> menuVos = buildLabel(0,this.baseMapper.getLabelMenuList(),new LinkedList<>());
        /*for (int i = 0;i<menuVos.size();i++){
            if(menuVos.get(i).getChildren()!=null && menuVos.get(i).getChildren().size()==1){
                menuVos.set(i,menuVos.get(i).getChildren().get(0));
            }
        }*/
        return menuVos;
    }

    @Override
    public int GetMaxMenuID() {
        return this.baseMapper.getMaxMenuID();
    }

    @Override
    public boolean UpdateMenusLabel(List<MenuVo> menuVos) {
        //拆解树结构
        List<MenuVo> chilren= TreeToListChilren(menuVos,new LinkedList<MenuVo>(),0);
        for(int i=0 ;i<menuVos.size();i++){
            menuVos.get(i).setOrder(i);
        }
        menuVos.addAll(chilren);
        //更新menuLabel
        for (MenuVo menuVo:menuVos){
            if(menuVo.isNewData()){
                Menu menu = new Menu();
                menu.setId(menuVo.getId());
                menu.setPid(menuVo.getPid());
                if (menuVo.getChildren()!=null&&menuVo.getChildren().size()>0)
                    menu.setComponent("Layout");
                menu.setMenuName(menuVo.getLabel());
                menu.setMenuSort(menuVo.getOrder());
                this.save(menu);
            }else{
                LambdaUpdateWrapper<Menu> wrapper = new LambdaUpdateWrapper<>();
                wrapper
                        .set(Menu::getPid,menuVo.getPid())
                        .set(Menu::getMenuName,menuVo.getLabel())
                        .set(Menu::getMenuSort,menuVo.getOrder())
                        .eq(Menu::getId,menuVo.getId());
                this.update(wrapper);
            }
        }
        return true;
    }

    @Override
    public boolean UpdateMenusData(Menu menu) {

        return this.updateById(menu);
    }

    private List<MenuVo> TreeToListChilren(List<MenuVo> menuVos,LinkedList<MenuVo> result,int pid){
        for(int i=0 ;i<menuVos.size();i++){
            MenuVo menuVo = menuVos.get(i);
            if(menuVo.getChildren()!=null&&menuVo.getChildren().size()!=0){
                TreeToListChilren(menuVo.getChildren(),result,menuVo.getId());
            }else {
                menuVo.setOrder(i);
                menuVo.setPid(pid);
                result.add(menuVo);
            }
        }
        return result;
    }

    private List<Map<String,Object>> buildTree(int pid,List<Menu> menus,List<Map<String,Object>> result){
        menus.forEach(menu -> {
            if(menu.getPid() == pid){
                Map<String,Object> r= initDta(menu);
                List<Map<String,Object>> temp = new LinkedList<>();
                List<Map<String,Object>> children=buildTree(menu.getId(),menus,temp);
                if( children.size() > 0)
                    r.put("children",children);
                result.add(r);
            }
        });
        return result;
    }

    private List<MenuVo> buildLabel(int pid,List<MenuVo> menus,List<MenuVo> result){
        menus.forEach(menu -> {
            if(menu.getPid() == pid){
                List<MenuVo> temp = new LinkedList<>();
                List<MenuVo> children=buildLabel(menu.getId(),menus,temp);
                if( children.size() > 0)
                    menu.setChildren(children);
                result.add(menu);
            }
        });
        return result;
    }

    private Map<String,Object> initDta(Menu menu){
        Map<String,Object> map = new HashMap<>();
        if(menu.getMenuName()!= null && !"".equals(menu.getMenuName())){
            if(menu.getPid()!=0||(menu.getIcon()!=null&&!"".equals(menu.getIcon()))){
                map.put("name",menu.getMenuName());
            }
        }
        if (menu.getComponent() == null||"".equals(menu.getComponent()) ) {
            map.put("component", "Layout");
        }else {
            map.put("component",menu.getComponent());
        }
        map.put("path",menu.getUrl());
        if (menu.getRedirect()!=null&& !"".equals(menu.getRedirect()))
            map.put("redirect",menu.getRedirect());
        if (menu.isHidden())
            map.put("hidden",true);
        Map<String,Object> meta = new HashMap<>();
        if(menu.getPid()!=0){
            meta.put("title",menu.getMenuName());
            meta.put("icon",menu.getIcon());
            if (menu.isAffix())
                meta.put("affix",true);
        }else{
            if(menu.getIcon()!=null&&!"".equals(menu.getIcon())){
                meta.put("title",menu.getMenuName());
            }
            meta.put("icon",menu.getIcon());
        }
        map.put("meta",meta);
        return map;
    }




}
