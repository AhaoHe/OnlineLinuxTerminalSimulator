package hmh.terminal.linux.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sun.mail.imap.protocol.ID;
import hmh.terminal.linux.dao.entity.Role;
import hmh.terminal.linux.dao.entity.RoleApi;
import hmh.terminal.linux.dao.entity.RoleMenu;
import hmh.terminal.linux.result.JsonResult;
import hmh.terminal.linux.result.ResultFactory;
import hmh.terminal.linux.service.RoleApiService;
import hmh.terminal.linux.service.RoleMenuService;
import hmh.terminal.linux.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/5/28 23:54
 */
@RestController
@RequestMapping("role")
@Slf4j
public class RoleController {
    @Autowired
    RoleService roleService;
    @Autowired
    RoleMenuService roleMenuService;
    @Autowired
    RoleApiService roleApiService;

    @GetMapping("info")
    public JsonResult GetInfo(){
        try{
            return  ResultFactory.SuccessResult(roleService.list());
        }catch (Exception e){
            return ResultFactory.FailResult("获取失败");
        }
    }

    @PostMapping("info")
    public JsonResult AddInfo(@RequestParam String info){
        try{
            JSONObject jsonObject = JSON.parseObject(info);
            Role role = (Role) JSONObject.toJavaObject(jsonObject,Role.class);
            if(role.getLevel()==1||role.getLevel()==10){
                throw new Exception("不允许新增系统角色");
            }
            return  ResultFactory.SuccessResult(roleService.save(role));
        }catch (Exception e){
            return ResultFactory.FailResult(e.getMessage());
        }
    }

    @PutMapping("info")
    public JsonResult UpdateInfo(@RequestParam("info") String info){
        try{
            JSONObject jsonObject= JSONObject.parseObject(info);
            Role role = (Role) JSONObject.toJavaObject(jsonObject,Role.class);
            if(role.getLevel()==1||role.getLevel()==10){
                throw new Exception("不允许修改系统角色");
            }
            return  ResultFactory.SuccessResult(roleService.updateById(role));
        }catch (Exception e){
            return ResultFactory.FailResult(e.getMessage());
        }
    }

    @DeleteMapping("info")
    public JsonResult DeleteInfo(@RequestParam("id") Integer id){
        try{
            //删除角色对应的菜单、API关联
            LambdaQueryWrapper<RoleMenu> queryWrapperMenu = new LambdaQueryWrapper<>();
            queryWrapperMenu.eq(RoleMenu::getRoleId,id);
            roleMenuService.remove(queryWrapperMenu);
            LambdaQueryWrapper<RoleApi> queryWrapperAPI = new LambdaQueryWrapper<>();
            queryWrapperAPI.eq(RoleApi::getRoleId,id);
            roleApiService.remove(queryWrapperAPI);
            return  ResultFactory.SuccessResult(roleService.removeById(id));
        }catch (Exception e){
            return ResultFactory.FailResult("获取失败");
        }
    }

    @GetMapping("rolemenu")
    public JsonResult GetRoleMenuChecked(@RequestParam("roleId")Integer roleId){
        try{
            LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper
                    .select(RoleMenu::getMenuId)
                    .eq(RoleMenu::getRoleId,roleId);
            return  ResultFactory.SuccessResult(roleMenuService.list(queryWrapper));
        }catch (Exception e){
            return ResultFactory.FailResult("获取失败");
        }
    }

    @GetMapping("roleapi")
    public JsonResult GetRoleAPIChecked(@RequestParam("roleId")Integer roleId){
        try{
            LambdaQueryWrapper<RoleApi> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper
                    .select(RoleApi::getApiId)
                    .eq(RoleApi::getRoleId,roleId);
            return  ResultFactory.SuccessResult(roleApiService.list(queryWrapper));
        }catch (Exception e){
            return ResultFactory.FailResult("获取失败");
        }
    }

    @PostMapping("rolemenu")
    public JsonResult RoleMenu(@RequestParam String menuIds, @RequestParam Integer roleId){
        try{
            int[] info= Arrays.stream(menuIds.split(",")).mapToInt(Integer::parseInt).toArray();
            return  ResultFactory.SuccessResult(roleMenuService.SaveRoleMenu(roleId,info));
        }catch (Exception e){
            return ResultFactory.FailResult("修改失败");
        }
    }

    @PostMapping("roleapi")
    public JsonResult RoleAPI(@RequestParam String apiIds, @RequestParam Integer roleId){
        try{
            int[] info= Arrays.stream(apiIds.split(",")).mapToInt(Integer::parseInt).toArray();
            return  ResultFactory.SuccessResult(roleApiService.SaveRoleApi(roleId,info));
        }catch (Exception e){
            return ResultFactory.FailResult("修改失败");
        }
    }

}
