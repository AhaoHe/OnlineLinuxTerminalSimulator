package hmh.terminal.linux.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import java.util.HashMap;
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
    @Autowired
    UserServerMapper userServerMapper;

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

    @Override
    public IPage<List<Map<String,Object>>> getSeverUsers(String query) {
        Map<String,Object> map = JSONObject.toJavaObject(JSON.parseObject(query),Map.class);
        IPage<Map<String,Object>> page=new Page<>((Integer)map.get("current"),(Integer)map.get("size"));
        //        List<Map<String, Object>> otherServerUsers = this.baseMapper.otherServerUsers(serverId);
//        Map<String,List> result = new HashMap<>();
//        result.put("thisSever",thisServerUsers);
//        result.put("otherSever",otherServerUsers);
        return this.baseMapper.thisServerUsers(page,(Integer)map.get("serverId"),(String)map.get("search"));
    }

    @Override
    public List<String> getServerSelection(Integer serverId) {
        return this.baseMapper.selectionSever(serverId);
    }

    @Override
    public boolean toNewServer(String users,Integer serverIdOld,Integer serverIdNew) {
        try {
            List<Map<String,Object>> usersdata = JSONArray.parseObject(users,List.class);
            //Map<String,Object> usersdata= JSONObject.toJavaObject(JSONObject.parseArray(users),Map.class);
            //String[] userdata= users.split(",");
            Map<String,Object> map=this.baseMapper.getNewServerByServerId(serverIdOld);
            Map<String,Object> mapNew=this.baseMapper.getNewServerByServerId(serverIdNew);
            SSHLinux sshLinux = new SSHLinux(map);
            SSHLinux sshLinuxNew = new SSHLinux(mapNew);
            if(sshLinuxNew.enable( Integer.parseInt((String) mapNew.get("status")) )&&sshLinux.enable( Integer.parseInt((String) map.get("status")) ) ){
                for(Map<String,Object> user:usersdata){
                    //获取转移的服务器的无组
//                    LambdaQueryWrapper<Group> queryWrapper = new LambdaQueryWrapper<>();
//                    queryWrapper.eq(Group::getServerId,serverIdNew).eq(Group::getStatus,0);
//                    List<Group> groups=groupMapper.selectList(queryWrapper);
                    sshLinuxNew.execute("useradd -g "+mapNew.get("group_name")+" "+user.get("server_username"));
                    //去除权限
                    sshLinux.execute("setfacl -b /home/"+user.get("server_username"));
                    sshLinux.execute("gpasswd -d "+user.get("server_username")+" "+user.get("group_name"));
                    //去除用户权限，去除用户组，,更新服务器ID
                    LambdaUpdateWrapper<UserServer> updateWrapper = new LambdaUpdateWrapper<>();
                    updateWrapper.set(UserServer::getServerId,serverIdNew)
                            .set(UserServer::getGroupId,mapNew.get("gid"))
                            .eq(UserServer::getUid,(Integer)user.get("uid"));
                    userServerMapper.update(null,updateWrapper);
                    sshLinux.close();
                    sshLinuxNew.close();
                }
            }else {
                sshLinux.close();
                sshLinuxNew.close();
                return false;
            }

            return true;
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
