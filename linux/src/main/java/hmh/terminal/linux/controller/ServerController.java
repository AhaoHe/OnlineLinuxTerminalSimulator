package hmh.terminal.linux.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import hmh.terminal.linux.dao.entity.Group;
import hmh.terminal.linux.dao.entity.Server;
import hmh.terminal.linux.dao.entity.UserServer;
import hmh.terminal.linux.result.JsonResult;
import hmh.terminal.linux.result.ResultFactory;
import hmh.terminal.linux.service.GroupService;
import hmh.terminal.linux.service.ServerService;
import hmh.terminal.linux.service.UserServerService;
import hmh.terminal.linux.vo.UserInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/5/20 21:03
 */
@RestController
@RequestMapping("server")
@Slf4j
public class ServerController {

    @Value("${filepath}")
    private String filepath = "D:\\JavaEE_Porject\\LinuxTerminalDemo\\";

    @Autowired
    ServerService serverService;
    @Autowired
    GroupService groupService;
    @Autowired
    UserServerService userServerService;

    /**
     * 查询所有数据集
     * @return
     */
    @GetMapping("info")
    public JsonResult getList() {
        try {
            List<Server> servers = serverService.getAllServer();
            return  ResultFactory.SuccessResult(servers);
        }catch (Exception e){
            e.printStackTrace();
            return  ResultFactory.FailResult("查询服务器出错!");
        }
    }

    @PostMapping("info")
    public  JsonResult insertServer(@RequestParam("server")String server){
        try {
            JSONObject jsonObject = JSONObject.parseObject(server);
            Server info = (Server) JSONObject.toJavaObject(jsonObject, Server.class);
            if(serverService.saveServerInfo(info)){
                return  ResultFactory.SuccessResult(info.getId());
            }else {
                return  ResultFactory.SuccessResult(false);
            }
        }catch (Exception e){
            e.printStackTrace();
            return  ResultFactory.FailResult("新增出错!");
        }
    }

    @PutMapping("info")
    public JsonResult updateServer(@RequestParam("server")String server){
        try {
            JSONObject jsonObject = JSONObject.parseObject(server);
            Server info = (Server) JSONObject.toJavaObject(jsonObject, Server.class);
            if(serverService.updateServerInfo(info)){
                return  ResultFactory.SuccessResult(serverService.getAllServer());
            }
            else return  ResultFactory.SuccessResult(false);
        }catch (Exception e){
            e.printStackTrace();
            return  ResultFactory.FailResult("修改出错!");
        }
    }

    @DeleteMapping("info")
    public JsonResult deleteServer(@RequestParam("id")int id){
        try {
            LambdaQueryWrapper<UserServer> userServerLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userServerLambdaQueryWrapper.select(UserServer::getUid).eq(UserServer::getServerId,id);
            //判断是否服务器中是否存在用户
            if(userServerService.list(userServerLambdaQueryWrapper).size()==0) {
                LambdaUpdateWrapper<Server> server = new LambdaUpdateWrapper<>();
                server.set(Server::getRegisterEnable, 0).eq(Server::getId, id);
                serverService.update(server);
                //删除组
                LambdaUpdateWrapper<Group> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.set(Group::getStatus, 7).eq(Group::getServerId, id);
                groupService.update(updateWrapper);
                return ResultFactory.SuccessResult(serverService.getAllServer());
            }else {
                return ResultFactory.SuccessResult(false);
            }
        }catch (Exception e){
            return  ResultFactory.FailResult("删除错误!");
        }
    }

    @PutMapping("register")
    public JsonResult updateServer(@RequestParam("id")int id){
        try {
            LambdaUpdateWrapper<Server> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(Server::getRegisterEnable,7);
            serverService.update(updateWrapper);
            LambdaUpdateWrapper<Server> wrapper = new LambdaUpdateWrapper<>();
            wrapper.set(Server::getRegisterEnable,3).eq(Server::getId,id);
            serverService.update(wrapper);
            return  ResultFactory.SuccessResult(serverService.getAllServer());
        }catch (Exception e){
            e.printStackTrace();
            return  ResultFactory.FailResult("修改出错!");
        }
    }


    @PostMapping("file")
    public JsonResult uploadFileKey(@RequestParam("file") MultipartFile file
                                    ,@RequestParam("id") int id){
        try {
            String path = filepath+System.currentTimeMillis();;
            //获取输出流
            OutputStream os=new FileOutputStream(path);
            //获取输入流 CommonsMultipartFile 中可以直接得到文件的流
            BufferedInputStream is=new BufferedInputStream(file.getInputStream());
            int temp;
            //一个一个字节的读取并写入
            while((temp=is.read())!=(-1))
            {
                os.write(temp);
            }
            os.flush();
            os.close();
            is.close();
            /*LambdaUpdateWrapper<Server> wrapper = new LambdaUpdateWrapper<>();
            wrapper.set(Server::getFileKey,path).eq(Server::getId,id);
            serverService.update(wrapper);*/
            return  ResultFactory.SuccessResult(path);
        }catch (Exception e){
            e.printStackTrace();
            return  ResultFactory.FailResult("上传出错!");
        }
    }

}
