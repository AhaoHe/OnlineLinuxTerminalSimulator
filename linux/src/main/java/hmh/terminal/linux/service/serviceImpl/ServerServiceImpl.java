package hmh.terminal.linux.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hmh.terminal.linux.dao.entity.Group;
import hmh.terminal.linux.dao.entity.Server;
import hmh.terminal.linux.dao.entity.UserServer;
import hmh.terminal.linux.dao.mapper.group.GroupMapper;
import hmh.terminal.linux.dao.mapper.server.ServerMapper;
import hmh.terminal.linux.dao.mapper.user_server.UserServerMapper;
import hmh.terminal.linux.service.SSHService;
import hmh.terminal.linux.service.ServerService;
import hmh.terminal.linux.service.UserServerService;
import hmh.terminal.linux.utils.SSHLinux;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.transform.Result;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/5/20 20:51
 */
@Service("ServerService")
@Slf4j
public class ServerServiceImpl extends  ServiceImpl<ServerMapper, Server> implements ServerService {

    @Value("${filepath}")
    private String filepath = "D:\\JavaEE_Porject\\LinuxTerminalDemo\\";

    @Autowired
    GroupMapper groupMapper;

    @Override
    public List<Server> getAllServer() {
        LambdaQueryWrapper<Server> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                /*.select(Server.class,info -> !info.getColumn().equals("filekey")
                        && !info.getColumn().equals("key") && !info.getColumn().equals("rootPassword"))*/
                .ne(Server::getRegisterEnable,0);
        List<Server> result = this.baseMapper.selectList(queryWrapper);
        for(Server server : result){
            String pass="";
            switch (server.getStatus()){
                case 0:
                    pass = server.getRootPassword();
                    break;
                case 1:
                    pass = server.getSqlkey();
                    break;
                case 2:
                    pass = server.getFileKey();
                    break;
            }
            if("".equals(pass)||pass==null){
                server.setEnable(false);
            }else {
                SSHLinux sshLinux = new SSHLinux(server.getHostname(),server.getPort(),pass);
                server.setEnable(sshLinux.enable(server.getStatus()));
            }
            server.setFileKey(null);
            server.setSqlkey(null);
            server.setRootPassword(null);
        }
        return result;
    }

    /*
    * 已废弃
    * */
    @Override
    public List<String> serverList() {
        return this.baseMapper.severList();
    }

    @Override
    public boolean updateServerInfo(Server server) {
        /*LambdaUpdateWrapper<Server> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .eq(Server::getId,server.getId());*/
        try {
            String key = getKey(server);
            SSHLinux sshLinux = new SSHLinux(server.getHostname(),server.getPort(),key);
            if(sshLinux.enable(server.getStatus())){
                this.baseMapper.updateById(server);
                return true;
            }else {
                if(server.getStatus()==2){
                    File file = new File(server.getFileKey());
                    if(file.exists())
                        file.delete();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean saveServerInfo(Server server) {
        try {
            String key = getKey(server);
            SSHLinux sshLinux = new SSHLinux(server.getHostname(),server.getPort(),key);
            if(sshLinux.enable(server.getStatus())){
                this.save(server);
                Map<String,Object> map=this.baseMapper.getServerByServerIdAndStatus(server.getId(),server.getStatus());
                map.put("server_username","");
                map.put("username","新建服务器");
                sshLinux=new SSHLinux(map);
                sshLinux.execute("groupadd noGroup");
                sshLinux.close();
                //新建组
                Group group =new Group();
                group.setName("无组");
                group.setGroupName("noGroup");
                group.setStatus(0);
                group.setFilter(true);
                group.setServerId(server.getId());
                groupMapper.insert(group);
                return true;
            }else {
                if(server.getStatus()==2){
                    File file = new File(server.getFileKey());
                    if(file.exists())
                        file.delete();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    private String getKey(Server server){
        String key ="";
        if(server.getSqlkey()==null&&server.getFileKey()==null&&server.getRootPassword()==null){
            Map<String,Object> pwdKey = this.baseMapper.getServerByServerIdAndStatus(server.getId(),server.getStatus());
            key = (String) pwdKey.get("loginKey");
        }else {
            switch (server.getStatus()){
                case 0:
                    key=server.getRootPassword();
                    break;
                case 1:
                    key=server.getSqlkey();
                    break;
                case 2:
                    key=server.getFileKey();
                    break;
                default:
                    break;
            }
        }
        return  key;
    }
}
