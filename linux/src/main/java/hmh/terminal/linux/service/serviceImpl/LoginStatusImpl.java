package hmh.terminal.linux.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hmh.terminal.linux.dao.entity.LoginStatus;
import hmh.terminal.linux.dao.mapper.loginstatus.LoginStatusMapper;
import hmh.terminal.linux.dao.mapper.menu.MenuMapper;
import hmh.terminal.linux.dao.mapper.user_server.UserServerMapper;
import hmh.terminal.linux.service.LoginStatusService;
import hmh.terminal.linux.service.UserServerService;
import hmh.terminal.linux.utils.SSHLinux;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Override
    public Map<String,List> getChartData(String startTime, int days) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        List<String> date = new LinkedList<>();
        String startDate=startTime;
        try {
            for (int i=0;i<days;i++){
                date.add(startDate);
                Date dd = df.parse(startDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dd);
                calendar.add(Calendar.DAY_OF_MONTH, 1);//加一天
                System.out.println("增加一天之后：" + df.format(calendar.getTime()));
                startDate = df.format(calendar.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<Map<String, String>> logindata = this.baseMapper.getChartData(startTime,startDate,Login);
        List<Map<String, String>> linuxdata =this.baseMapper.getChartData(startTime,startTime,Linux);
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
        map.put("logindata",login);
        map.put("linuxdata",linux);
        map.put("date",date);
        map.put("x",new LinkedList(){{add("Linux活跃人数");add("平台活跃人数");}});
        return map;
    }

    @Override
    public List<String> getDFH(String username) {
        SSHLinux sshLinux = new SSHLinux(userServerMapper.getUserServerByUserName(username,null));
        String encodeSet = "export LC_CTYPE=zh_CN.GB18030;";
        List<String> result= sshLinux.execute(encodeSet+"df -h|grep /dev/vda1");
        return result;
    }

}
