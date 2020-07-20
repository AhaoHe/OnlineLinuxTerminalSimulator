package hmh.terminal.linux.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import hmh.terminal.linux.dao.entity.Command;
import hmh.terminal.linux.dao.entity.User;
import hmh.terminal.linux.dto.ManageUserDTO;
import hmh.terminal.linux.result.JsonResult;
import hmh.terminal.linux.result.ResultFactory;
import hmh.terminal.linux.service.CommandService;
import hmh.terminal.linux.vo.CommandVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/5/5 20:57
 */
@RestController
@RequestMapping("command")
public class CommandController {
    @Autowired
    CommandService commandService;
    /**
     * 查询所有类似的命令行
     * @return
     */
    @GetMapping("findLikeCommand")
    public JsonResult getList(@RequestParam("command") String command) {
        try {
            List<Command> list = commandService.findAllLikeCommand(command);
            return ResultFactory.SuccessResult(list);
        }catch (Exception e){
            return  ResultFactory.FailResult("获取失败！");
        }
    }

    @GetMapping("info")
    public JsonResult info(@RequestParam("info") String info) {
        try {
            JSONObject jsonObject = JSON.parseObject(info);
            CommandVo commandVo = JSONObject.toJavaObject(jsonObject,CommandVo.class);
            IPage<List<Command>> list = commandService.getInfo(commandVo);
            return ResultFactory.SuccessResult(list);
        }catch (Exception e){
            return  ResultFactory.FailResult("获取失败！");
        }
    }

    @PostMapping("info")
    public JsonResult addCommand(@RequestParam("info") String info){
        try {
            JSONObject jsonObject = JSON.parseObject(info);
            Command command = JSONObject.toJavaObject(jsonObject,Command.class);
            command.setStatus(3);
            return ResultFactory.SuccessResult(commandService.save(command));
        }catch (Exception e){
            return  ResultFactory.FailResult("添加失败！");
        }
    }

    @PutMapping("info")
    public JsonResult updateCommand(@RequestParam("info") String info){
        try {
            JSONObject jsonObject = JSON.parseObject(info);
            Command command = JSONObject.toJavaObject(jsonObject,Command.class);
            command.setStatus(3);
            return ResultFactory.SuccessResult(commandService.updateById(command));
        }catch (Exception e){
            return  ResultFactory.FailResult("修改失败！");
        }
    }

    @DeleteMapping("info")
    public JsonResult delCommand(@RequestParam("id") Integer id){
        try {
            LambdaUpdateWrapper<Command> updateWrapper= new LambdaUpdateWrapper<>();
            updateWrapper.set(Command::getStatus,7)
                    .eq(Command::getId,id);
            return ResultFactory.SuccessResult(commandService.update(updateWrapper));
        }catch (Exception e){
            return  ResultFactory.FailResult("删除失败！");
        }
    }

    @PostMapping("change")
    public JsonResult changeCommandStatus(@RequestParam("selection") String selection,@RequestParam("unselection") String unselection){
        try {
            /*String[] select = selection.split(",");
            String[] unselect = unselection.split(",");
            List<Command> checked= new LinkedList<>();
            List<Command> unchecked= new LinkedList<>();
            for (){

            }*/
            commandService.changeStatus(selection,0);
            commandService.changeStatus(unselection,3);
            return ResultFactory.SuccessResult(true);
        }catch (Exception e){
            return  ResultFactory.FailResult("修改失败！");
        }
    }

    @GetMapping("groupcommand")
    public JsonResult groupcommand(@RequestParam("groupId")Integer groupId){
        try {
            return  ResultFactory.SuccessResult(commandService.getGroupCommand(groupId));
        }catch (Exception e){
            return ResultFactory.FailResult("查询失败");
        }
    }

    @PostMapping("command")
    public JsonResult GroupCommand(@RequestParam("groupId")Integer groupId,@RequestParam("selection") String selection,@RequestParam("unselection") String unselection){
        try{
            return  ResultFactory.SuccessResult(commandService.groupCommand(groupId,selection,unselection));
        }catch (Exception e){
            return  ResultFactory.FailResult(e.getMessage());
        }
    }
}
