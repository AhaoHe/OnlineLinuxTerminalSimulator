package hmh.terminal.linux.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.sun.org.apache.bcel.internal.generic.NEW;
import hmh.terminal.linux.dao.entity.UserServer;
import hmh.terminal.linux.result.JsonResult;
import hmh.terminal.linux.result.ResultFactory;
import hmh.terminal.linux.service.LoginStatusService;
import hmh.terminal.linux.service.UserServerService;
import hmh.terminal.linux.utils.SSHLinux;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/2/6 15:24
 */
//@Controller
@RestController
@RequestMapping("home")
@Slf4j
public class HomeController {

    /*@RequestMapping("/ws")
    public String toIndex(){
        return "index";
    }*/

    @Autowired
    LoginStatusService loginStatusService;
    @Autowired
    UserServerService userServerService;


    //获取time往前days天的数据
    @GetMapping("echarts")
    public JsonResult echarts(@RequestParam("time")String time,
                              @RequestParam("days")Integer days){
        try{
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date dd = df.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dd);
            calendar.add(Calendar.DAY_OF_MONTH, -days);//加一天
            System.out.println("开始的时间" + df.format(calendar.getTime()));
            String startTime = df.format(calendar.getTime());
            return  ResultFactory.SuccessResult(loginStatusService.getChartData(startTime,days));
        }catch (Exception e){
            return  ResultFactory.FailResult(e.getMessage());
        }
    }

    @GetMapping("onlinecount")
    public JsonResult onlineCount(){
        try{
            return  ResultFactory.SuccessResult(WebSocketServer.onlineCount);
        }catch (Exception e){
            return  ResultFactory.FailResult(e.getMessage());
        }
    }

    @GetMapping("online")
    public JsonResult online(){
        try{
            ConcurrentHashMap<String,WebSocketServer> temp;
            temp = WebSocketServer.webSocketMap;
            List<String> list = new LinkedList<>(temp.keySet());
            return  ResultFactory.SuccessResult(list);
        }catch (Exception e){
            return  ResultFactory.FailResult(e.getMessage());
        }
    }

    @DeleteMapping("online")
    public JsonResult online(@RequestParam("username")String username){
        try{
            Thread thread = new Thread(new Runnable(){
                @Override
                public void run() {
                    try {
                        WebSocketServer.webSocketMap.get(username).sendMessage("管理员已将您的服务器下线！");
                        WebSocketServer.webSocketMap.get(username).onClose();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
            return  ResultFactory.SuccessResult(true);
        }catch (Exception e){
            return  ResultFactory.FailResult(e.getMessage());
        }
    }

    @PutMapping("online")
    public JsonResult ban(@RequestParam("username")String username){
        try{
            Thread thread = new Thread(new Runnable(){
                @Override
                public void run() {
                    try {
                        WebSocketServer.webSocketMap.get(username).sendMessage("管理员已将封禁！");
                        WebSocketServer.webSocketMap.get(username).onClose();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
            return  ResultFactory.SuccessResult(userServerService.BanUser(username));
        }catch (Exception e){
            return  ResultFactory.FailResult(e.getMessage());
        }
    }


    @GetMapping("dfh")
    public JsonResult dfh(@RequestParam("username")String username){
        try{
            return  ResultFactory.SuccessResult(loginStatusService.getDFH(username));
        }catch (Exception e){
            return  ResultFactory.FailResult(e.getMessage());
        }
    }

    @GetMapping("disk")//获取所有用户磁盘分配情况
    public JsonResult disk(@RequestParam("current")Integer current,@RequestParam("size")Integer size
            ,@RequestParam("search")String search,@RequestParam("level")Integer level,@RequestParam("groupId")Integer groupId){
        try{
            return  ResultFactory.SuccessResult(userServerService.disk(current,size,search,level,groupId));
        }catch (Exception e){
            return  ResultFactory.FailResult(e.getMessage());
        }
    }

    @GetMapping("diskuser")
    public JsonResult diskUser(@RequestParam("uid")Integer uid,@RequestParam("username")String username){
        try{
            return  ResultFactory.SuccessResult(userServerService.diskUser(uid,username).get(7));
        }catch (Exception e){
            return  ResultFactory.FailResult(e.getMessage());
        }
    }

    @PutMapping("disk")
    public JsonResult disk(@RequestParam("uid")Integer uid,@RequestParam("username")String username,@RequestParam("disk")String disk){
        try{
            return  ResultFactory.SuccessResult(userServerService.updateDisk(uid,username,disk));
        }catch (Exception e){
            e.printStackTrace();
            return  ResultFactory.FailResult(e.getMessage());
        }
    }
}
