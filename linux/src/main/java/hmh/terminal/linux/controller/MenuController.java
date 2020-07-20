package hmh.terminal.linux.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import hmh.terminal.linux.dao.entity.Menu;
import hmh.terminal.linux.result.JsonResult;
import hmh.terminal.linux.result.ResultFactory;
import hmh.terminal.linux.service.MenuService;
import hmh.terminal.linux.service.RoleMenuService;
import hmh.terminal.linux.vo.MenuVo;
import hmh.terminal.linux.vo.UserInfoVo;
import hmh.terminal.linux.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/5/22 21:30
 */
@RestController
@RequestMapping("menu")
@Slf4j
public class MenuController {
    @Autowired
    MenuService menuService;
    @Autowired
    RoleMenuService roleMenuService;

    @GetMapping("label")
    public JsonResult label() {
        try {
            Map<String,Object> map = new HashMap<>();
            map.put("maxId",menuService.GetMaxMenuID());
            map.put("data",menuService.buildMenusLabel());
            return  ResultFactory.SuccessResult(map);
        }catch (Exception e){
            return ResultFactory.FailResult(e.getMessage());
        }
    }

    @GetMapping("data")
    public JsonResult data(@RequestParam("id") Integer id) {
        try {
            return  ResultFactory.SuccessResult(menuService.getMenuDataByID(id));
        }catch (Exception e){
            return ResultFactory.FailResult(e.getMessage());
        }
    }

    @PostMapping("data")
    public JsonResult setData(@RequestParam("menu") String menu) {
        try {
            JSONObject jsonObject=JSONObject.parseObject(menu);
            Menu info=(Menu) JSONObject.toJavaObject(jsonObject, Menu.class);
            return  ResultFactory.SuccessResult(menuService.UpdateMenusData(info));
        }catch (Exception e){
            return ResultFactory.FailResult(e.getMessage());
        }
    }

    @PostMapping("label")
    public JsonResult updateLabel(@RequestBody JSONObject menuVo){
        try {
            //JSONObject jsonObject = JSONObject.parseObject(menuVo);
            //JSONArray jsonArray= JSONArray.parseArray(menuVo);
            List<MenuVo> info = (List<MenuVo>) JSONArray.parseArray(menuVo.getString("menuVo"), MenuVo.class);

            return  ResultFactory.SuccessResult(menuService.UpdateMenusLabel(info));
        }catch (Exception e){
            e.printStackTrace();
            return ResultFactory.FailResult("修改失败");
        }
    }


    @DeleteMapping("label")
    public JsonResult deleteLabel(@RequestParam("ids") String ids){
        try {
            int[] info= Arrays.stream(ids.split(",")).mapToInt(Integer::parseInt).toArray();
            List<Integer> infos=Arrays.stream(info).boxed().collect(Collectors.toList());
            roleMenuService.DelRoleMenu(infos);
            return  ResultFactory.SuccessResult(menuService.removeByIds(infos));
        }catch (Exception e){
            e.printStackTrace();
            return ResultFactory.FailResult("删除失败！");
        }
    }

}
