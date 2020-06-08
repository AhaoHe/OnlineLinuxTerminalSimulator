package hmh.terminal.linux.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import hmh.terminal.linux.dao.entity.Group;
import hmh.terminal.linux.dao.entity.GroupCommand;
import hmh.terminal.linux.dao.entity.Server;
import hmh.terminal.linux.result.JsonResult;
import hmh.terminal.linux.result.ResultFactory;
import hmh.terminal.linux.service.GroupService;
import hmh.terminal.linux.service.ServerService;
import hmh.terminal.linux.service.UserService;
import hmh.terminal.linux.vo.GroupVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.java2d.windows.GDIRenderer;

import java.util.List;
import java.util.Map;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/6/4 20:01
 */
@RestController
@RequestMapping("group")
public class GroupController {
    @Autowired
    GroupService groupService;

    @Autowired
    ServerService serverService;

    @GetMapping("info")
    public JsonResult info(@RequestParam("level") int level,@RequestParam("groupId") int groupId){
        try{
            return  ResultFactory.SuccessResult(groupService.getGroupList(level,groupId));
        }catch (Exception e){
            return  ResultFactory.FailResult(e.getMessage());
        }
    }

    @PostMapping("info")
    public JsonResult SaveOrUpdate(@RequestParam("info") String info){
        try{
            JSONObject jsonObject = JSON.parseObject(info);
            Group group = JSONObject.toJavaObject(jsonObject,Group.class);
            group.setStatus(3);
            return  ResultFactory.SuccessResult(groupService.SaveOrUpdateGroup(group));
        }catch (Exception e){
            return  ResultFactory.FailResult(e.getMessage());
        }
    }
    @DeleteMapping("info")
    public JsonResult delete(@RequestParam("groupId") int groupId){
        try{
            return  ResultFactory.SuccessResult(groupService.delGroup(groupId));
        }catch (Exception e){
            return  ResultFactory.FailResult(e.getMessage());
        }
    }

    @GetMapping("userchecked")
    public JsonResult unchecked(@RequestParam("groupVo") String info){
        try{
            JSONObject jsonObject = JSON.parseObject(info);
            GroupVo groupVo = JSONObject.toJavaObject(jsonObject,GroupVo.class);
            List<Map<String,Object>> checked= groupService.getUserChecked(groupVo);
            return  ResultFactory.SuccessResult(checked);
        }catch (Exception e){
            return  ResultFactory.FailResult(e.getMessage());
        }
    }

    @GetMapping("userunchecked")
    public JsonResult checked(@RequestParam("groupVo") String info){
        try{
            JSONObject jsonObject = JSON.parseObject(info);
            GroupVo groupVo = JSONObject.toJavaObject(jsonObject,GroupVo.class);
            return  ResultFactory.SuccessResult(groupService.getUserUnchecked(groupVo));
        }catch (Exception e){
            return  ResultFactory.FailResult(e.getMessage());
        }
    }

    @GetMapping("applyunchecked")
    public JsonResult applyunchecked(@RequestParam("groupId") Integer groupId,@RequestParam("search") String search){
        try{
            return  ResultFactory.SuccessResult(groupService.getApplyUnChecked(groupId,search));
        }catch (Exception e){
            return  ResultFactory.FailResult(e.getMessage());
        }
    }

    @PostMapping("addUser")//添加到组中
    public JsonResult addUserToGroup(@RequestParam("strArr") String strArr,@RequestParam("groupId") Integer groupId){
        try{
            List<Map<String,Object>> users = JSONArray.parseObject(strArr,List.class);
            return  ResultFactory.SuccessResult(groupService.addUserToGroup(users,groupId));
        }catch (Exception e){
            return  ResultFactory.FailResult(e.getMessage());
        }
    }

    @PostMapping("removeUser")//从组中移除
    public JsonResult removeUserToGroup(@RequestParam("strArr") String strArr,@RequestParam("groupId") Integer groupId){
        try{
            List<Map<String,Object>> users = JSONArray.parseObject(strArr,List.class);
            return  ResultFactory.SuccessResult(groupService.removeUserToGroup(users,groupId));
        }catch (Exception e){
            return  ResultFactory.FailResult(e.getMessage());
        }
    }

    @GetMapping("server")
    public JsonResult serverList(){
        try{
            return  ResultFactory.SuccessResult(serverService.serverList());
        }catch (Exception e){
            return  ResultFactory.FailResult(e.getMessage());
        }
    }

    @GetMapping("exist")
    public JsonResult exist(@Param("groupName")String groupName,@Param("serverId")Integer serverId){
        try{
            LambdaQueryWrapper<Group> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper
                    .eq(Group::getGroupName,groupName)
                    .eq(Group::getServerId,serverId);
            int count = groupService.list(queryWrapper).size();
            if(count==0){
                return  ResultFactory.SuccessResult(true);
            }
            return  ResultFactory.SuccessResult(false);
        }catch (Exception e){
            return  ResultFactory.FailResult(e.getMessage());
        }
    }

    /*
    * 申请组
    * */
    @GetMapping("apply")
    public JsonResult apply(@RequestParam("username") String username,@RequestParam("groupId") Integer groupId){
        try{
            return  ResultFactory.SuccessResult(groupService.applyGroupList(username,groupId));
        }catch (Exception e){
            return  ResultFactory.FailResult("查询失败");
        }
    }

    @PostMapping("apply")
    public JsonResult applyGroup(@RequestParam("groupId") int groupId,@RequestParam("username")String username){
        try{
            return  ResultFactory.SuccessResult(groupService.ApplyGroup(groupId,username));
        }catch (Exception e){
            return  ResultFactory.FailResult(e.getMessage());
        }
    }
    @DeleteMapping("apply")
    public JsonResult delApply(@RequestParam("groupId") int groupId,@RequestParam("usernames")String usernames){
        try{
            return  ResultFactory.SuccessResult(groupService.delApply(groupId,usernames));
        }catch (Exception e){
            return  ResultFactory.FailResult(e.getMessage());
        }
    }

}
