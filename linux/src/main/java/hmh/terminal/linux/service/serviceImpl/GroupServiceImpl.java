package hmh.terminal.linux.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.org.apache.bcel.internal.generic.NEW;
import hmh.terminal.linux.dao.entity.Group;
import hmh.terminal.linux.dao.entity.GroupCommand;
import hmh.terminal.linux.dao.entity.Role;
import hmh.terminal.linux.dao.entity.UserServer;
import hmh.terminal.linux.dao.mapper.group.GroupMapper;
import hmh.terminal.linux.dao.mapper.groupcommand.GroupCommandMapper;
import hmh.terminal.linux.dao.mapper.user_server.UserServerMapper;
import hmh.terminal.linux.service.GroupService;
import hmh.terminal.linux.service.UserServerService;
import hmh.terminal.linux.service.UserService;
import hmh.terminal.linux.utils.SSHLinux;
import hmh.terminal.linux.vo.GroupVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/6/4 20:04
 */
@Service("GroupService")
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements GroupService {

    @Autowired
    UserServerMapper userServerMapper;

    @Override
    public List<Group> getGroupList(int level,int groupId) {
        LambdaQueryWrapper<Group> group = new LambdaQueryWrapper<>();
        if(level==10){
            group.ne(Group::getStatus,7);
        }else {
            group
                    .eq(Group::getId,groupId)
                    .eq(Group::getStatus,3);//组管理员只能获取不是无组和删除的
        }
        return this.baseMapper.selectList(group);
    }

    @Override
    public boolean SaveOrUpdateGroup(Group group) {
        if(group.getId()!=-1){
            try {
                Map<String,Object> sever = this.baseMapper.getGroupDefault(group.getId());
                //判断是否能连接服务器
                SSHLinux ssh = new SSHLinux((String) sever.get("hostname"),(Integer) sever.get("port"),(String)sever.get("loginKey"));
                if(!ssh.enable(Integer.parseInt((String) sever.get("status"))) ){
                    return false;
                }
                //更新
                if(!group.getGroupName().equals(sever.get("group_name")) ){
                    SSHLinux sshLinux = new SSHLinux(sever);
                    sshLinux.execute("groupmod -n "+group.getGroupName()+" "+sever.get("group_name"));
                    sshLinux.close();
                }
                this.baseMapper.updateById(group);
                return true;
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            try {
                LambdaQueryWrapper<Group> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper
                        .eq(Group::getGroupName,group.getGroupName())
                        .eq(Group::getServerId,group.getServerId());
                //判断是否已经存在同服务器、同组名的组
                if (this.baseMapper.selectList(queryWrapper).size()>0)
                    return false;
                //判断是否可以连接服务器
                Map<String,Object> sever = this.baseMapper.getGroupServer(group.getServerId());
                SSHLinux ssh = new SSHLinux((String) sever.get("hostname"),(Integer) sever.get("port"),(String)sever.get("loginKey"));
                if(!ssh.enable(Integer.parseInt((String) sever.get("status"))) ){
                    return false;
                }
                //添加
                int count=this.baseMapper.insert(group);
                if(count>0){
                    SSHLinux sshLinux = new SSHLinux(sever);
                    sshLinux.execute("groupadd "+group.getGroupName());
                    sshLinux.close();
                }
                return  true;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean delGroup(int groupId) {
        try {
            //获取原组所有的成员,删除所有权限,移除附加组
            List<String> users = this.baseMapper.getGroupUser(groupId);//原组员
            Map<String,Object> sever = this.baseMapper.getGroupDefault(groupId);
            SSHLinux sshLinux = new SSHLinux(sever);
            for (String sname:users){
                sshLinux.execute("setfacl -b /home/"+sname);
                sshLinux.execute("gpasswd -d "+sname+" "+sever.get("group_name"));
            }
            sshLinux.close();
            Integer groupIdDefault = this.baseMapper.getGroupIdDefault(groupId);
            //用户全部变成无组
            LambdaUpdateWrapper<UserServer> updateWrapperUser = new LambdaUpdateWrapper<>();
            updateWrapperUser
                    .set(UserServer::getGroupId,groupIdDefault)
                    .eq(UserServer::getGroupId,groupId);
            userServerMapper.update(null,updateWrapperUser);
            //假删除
            LambdaUpdateWrapper<Group> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper
                    .set(Group::getStatus,7)
                    .eq(Group::getId,groupId);
            this.baseMapper.update(null,updateWrapper);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Map<String,Object>> getUserUnchecked(GroupVo groupVo) {
        return this.baseMapper.getUserUnchecked(groupVo);
    }

    @Override
    public List<Map<String, Object>> getApplyUnChecked(Integer groupId,String search) {
        return this.baseMapper.getApplyUnchecked(groupId,search);
    }

    @Override
    public List<Map<String,Object>> getUserChecked(GroupVo groupVo) {
        return this.baseMapper.getUserChecked(groupVo);
    }

    @Override
    public boolean addUserToGroup(List<Map<String,Object>> users, Integer groupId) {
        try {
            Map<String,Object> map = this.baseMapper.getGroupDefault(groupId);
            List<String> admins=new LinkedList<>();
            List<String> oldadmins = userServerMapper.getGroupServerAdminUserName(groupId);
            if(0==(Integer) map.get("group_status") ){//为该服务器的无组
                //去除所有人权限
                SSHLinux sshLinux=new SSHLinux(map);
                for (Map<String,Object> user:users){
                    sshLinux.execute("setfacl -b /home/"+user.get("server_username"));
                    sshLinux.execute("gpasswd -d "+user.get("server_username")+" "+user.get("group_name"));
                    LambdaUpdateWrapper<UserServer> wrapper = new LambdaUpdateWrapper<>();
                    wrapper.set(UserServer::getGroupId,groupId).eq(UserServer::getUid,user.get("uid"));
                    userServerMapper.update(null,wrapper);
                }
                sshLinux.close();
                return true;
            }else {//不是无组
                SSHLinux sshLinux=new SSHLinux(map);
                for (Map<String,Object> user:users){
                    sshLinux.execute("setfacl -b /home/"+user.get("server_username"));
                    sshLinux.execute("usermod -G "+map.get("group_name")+" "+user.get("server_username"));
                    LambdaUpdateWrapper<UserServer> wrapper = new LambdaUpdateWrapper<>();
                    wrapper.set(UserServer::getGroupId,groupId).eq(UserServer::getUid,user.get("uid"));
                    userServerMapper.update(null,wrapper);
                    if((Integer)user.get("level")>=8&&(Integer)user.get("level")<10){
                        admins.add((String) user.get("server_username"));
                    }else if((Integer)user.get("level")<8){
                        //给之前的管理员增加对新组员的权限
                        for(String susername : oldadmins){
                            sshLinux.execute("setfacl -m u:"+susername+":rwx /home/"+user.get("server_username"));
                        }
                        //给新组员去掉申请信息
                        this.baseMapper.delApply(groupId,(String) user.get("username"));
                    }
                }
                //新管理员增加对所有普通用户的权限
                List<String> serverUserNames= userServerMapper.getGroupServerUserName(groupId);
                for(String admin : admins){
                    for (String susername:serverUserNames)
                        sshLinux.execute("setfacl -m u:"+admin+":rwx /home/"+susername);
                }
                sshLinux.close();
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removeUserToGroup(List<Map<String, Object>> users, Integer groupId) {
        try {
            Integer groupIdDefault = this.baseMapper.getGroupIdDefault(groupId);
            Map<String,Object> map = this.baseMapper.getGroupDefault(groupId);
            if(0==(Integer) map.get("group_status") ){//为该服务器的无组
                return false;
            }else {//不是无组
                //去除所有人权限
                removePower(users,groupIdDefault,map);
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean RoleGroup(Integer groupId, Integer level,String susername) {
        try {
            if(groupId!=this.baseMapper.getGroupIdDefault(groupId)){
                Map<String,Object> map = this.baseMapper.getGroupDefault(groupId);
                List<String> users =this.baseMapper.getGroupUser(groupId);
                SSHLinux sshLinux = new SSHLinux(map);
                if(level>=8&&level<10){
                    for(String user:users){
                        sshLinux.execute("setfacl -m u:"+susername+":rwx /home/"+user);
                    }
                }else {
                    for(String user:users){
                        sshLinux.execute("setfacl -x u:"+susername+":rwx /home/"+user);
                    }
                }
                sshLinux.close();
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  false;
    }

    @Override
    public List<Map<String, Object>> applyGroupList(String username,Integer groupId) {
        return this.baseMapper.applyGroupList(username,groupId);
    }

    @Override
    public boolean ApplyGroup(Integer groupId, String username) {
        if(this.baseMapper.isOneApply(groupId,username)==0){
            this.baseMapper.applyGroup(groupId,username);
            return true;
        }
        return false;
    }

    @Override
    public boolean delApply(Integer groupId, String usernames) {
        return this.baseMapper.delApply(groupId,usernames)>0;
    }


    private void removePower(List<Map<String,Object>> users,Integer groupId,Map<String,Object> map){
        SSHLinux sshLinux=new SSHLinux(map);
        for (Map<String,Object> user:users){
            sshLinux.execute("setfacl -b /home/"+user.get("server_username"));
            sshLinux.execute("gpasswd -d "+user.get("server_username")+" "+map.get("group_name"));
            LambdaUpdateWrapper<UserServer> wrapper = new LambdaUpdateWrapper<>();
            wrapper.set(UserServer::getGroupId,groupId).eq(UserServer::getUid,user.get("uid"));
            userServerMapper.update(null,wrapper);
        }
        sshLinux.close();
    }


}
