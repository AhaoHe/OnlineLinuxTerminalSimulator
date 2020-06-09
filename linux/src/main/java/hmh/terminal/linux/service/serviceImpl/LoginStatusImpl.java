package hmh.terminal.linux.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hmh.terminal.linux.controller.WebSocketServer;
import hmh.terminal.linux.dao.entity.Command;
import hmh.terminal.linux.dao.entity.LoginStatus;
import hmh.terminal.linux.dao.entity.UserCommand;
import hmh.terminal.linux.dao.mapper.loginstatus.LoginStatusMapper;
import hmh.terminal.linux.dao.mapper.menu.MenuMapper;
import hmh.terminal.linux.dao.mapper.user_command.UserCommandMapper;
import hmh.terminal.linux.dao.mapper.user_server.UserServerMapper;
import hmh.terminal.linux.dto.UserOnlineDTO;
import hmh.terminal.linux.service.LoginStatusService;
import hmh.terminal.linux.service.UserServerService;
import hmh.terminal.linux.utils.SSHLinux;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Size;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/6/1 21:59
 */
@Service("LoginStatusService")
public class LoginStatusImpl extends ServiceImpl<LoginStatusMapper,LoginStatus> implements LoginStatusService {
    protected final int Login=0;
    protected final int Linux=1;

    @Autowired
    UserServerMapper userServerMapper;
    @Autowired
    UserCommandMapper userCommandMapper;

    private String getUsersOnline(){
        ConcurrentHashMap<String, WebSocketServer> temp;
        temp = WebSocketServer.webSocketMap;
        List<String> list = new LinkedList<>(temp.keySet());
        return String.join(",", list);
    }

    @Override
    public IPage<List<UserOnlineDTO>> getListNoBan(String request) {
        Map<String,Object> result = JSONObject.toJavaObject(JSON.parseObject(request),Map.class);
        IPage<UserOnlineDTO> page = new Page<>((Integer)result.get("current"), (Integer)result.get("size"));
        return this.baseMapper.getListOnline(page,getUsersOnline(),result,1);
    }

    @Override
    public IPage<List<UserOnlineDTO>> getListWithBan(String request) {
        Map<String,Object> result = JSONObject.toJavaObject(JSON.parseObject(request),Map.class);
        IPage<UserOnlineDTO> page = new Page<>((Integer)result.get("current"), (Integer)result.get("size"));
        return this.baseMapper.getListOnline(page,getUsersOnline(),result,0);
    }

    @Override
    public IPage<UserCommand> getHistoryCommand(String request) {
        Map<String,Object> result = JSONObject.toJavaObject(JSON.parseObject(request),Map.class);
        IPage<UserCommand> page = new Page<>((Integer)result.get("current"), (Integer)result.get("size"));
        LambdaQueryWrapper<UserCommand> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserCommand::getUsername,result.get("username"));
        return userCommandMapper.selectPage(page,queryWrapper);
    }


    /*
    * 获取echart的数据
    * */


    @Override
    public Map<String,List> getChartData(String startTime, int days) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            List<String> date = new LinkedList<>();
            String endDate=startTime;
            if(startTime==null){
                throw  new Exception("获取失败");
            }
            for (int i=0;i<days;i++){
                date.add(endDate);
                Date dd = df.parse(endDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dd);
                calendar.add(Calendar.DAY_OF_MONTH, 1);//加一天
                System.out.println("增加一天之后：" + df.format(calendar.getTime()));
                endDate = df.format(calendar.getTime());
            }
            List<Map<String, String>> logindata = this.baseMapper.getChartData(startTime,endDate,Login);
            List<Map<String, String>> linuxdata =this.baseMapper.getChartData(startTime,endDate,Linux);
            Map<String,List> map = new HashMap<>();
            List<Integer> login = new LinkedList<>();
            List<Integer> linux = new LinkedList<>();
            for (String d:date){
                String logincount="0";
                String linuxcount="0";
                for (Map<String, String> data : logindata) {
                    if(d.equals(data.get("dat"))){
                        logincount= data.get("count");
                    }
                }
                for (Map<String, String> data : linuxdata) {
                    if (d.equals(data.get("dat"))) {
                        linuxcount= data.get("count");
                    }
                }
                login.add(Integer.parseInt(logincount) );
                linux.add(Integer.parseInt(linuxcount) );
            }
            map.put("firstData",login);
            map.put("secondData",linux);
            map.put("dateData",date);
            map.put("xData",new LinkedList(){{add("平台活跃人数");add("Linux活跃人数");}});
            return map;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, List> getUserChartData(String startTime, int days, String username) {
        try{
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            List<String> date = new LinkedList<>();
            String endDate=startTime;
            if(startTime==null){
                throw  new Exception("获取失败");
            }
            for (int i=0;i<days;i++){
                date.add(endDate);
                Date dd = df.parse(endDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dd);
                calendar.add(Calendar.DAY_OF_MONTH, 1);//加一天
                System.out.println("增加一天之后：" + df.format(calendar.getTime()));
                endDate = df.format(calendar.getTime());
            }
            List<Map<String, String>> userdata = this.baseMapper.getUserData(startTime,endDate,username);
            Map<String,List> map = new HashMap<>();
            List<Integer> countList = new LinkedList<>();
            List<Integer> dangercountList = new LinkedList<>();
            for (String d:date){
                String count="0";
                String dangercount="0";
                for (Map<String, String> data : userdata) {
                    if(d.equals(data.get("dat"))){
                        count= data.get("count");
                        dangercount = data.get("dangercount");
                    }
                }
                countList.add(Integer.parseInt(count) );
                dangercountList.add(Integer.parseInt(dangercount) );
            }
            map.put("firstData",countList);
            map.put("secondData",dangercountList);
            map.put("dateData",date);
            map.put("xData",new LinkedList(){{add("代码量");add("危险代码数");}});
            return map;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<String> getDFH(String username) {
        SSHLinux sshLinux = new SSHLinux(userServerMapper.getUserServerByUserName(username,null));
        String encodeSet = "export LC_CTYPE=zh_CN.GB18030;";
        List<String> result= sshLinux.execute(encodeSet+"df -h|grep /dev/vda1");
        return result;
    }

}
