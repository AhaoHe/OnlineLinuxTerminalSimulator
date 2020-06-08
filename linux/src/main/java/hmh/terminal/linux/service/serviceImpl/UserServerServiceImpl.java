package hmh.terminal.linux.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hmh.terminal.linux.dao.entity.Api;
import hmh.terminal.linux.dao.entity.LoginStatus;
import hmh.terminal.linux.dao.entity.UserServer;
import hmh.terminal.linux.dao.mapper.loginstatus.LoginStatusMapper;
import hmh.terminal.linux.dao.mapper.user_server.UserServerMapper;
import hmh.terminal.linux.dto.DiskDTO;
import hmh.terminal.linux.service.LoginStatusService;
import hmh.terminal.linux.service.UserServerService;
import hmh.terminal.linux.utils.SSHLinux;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/6/3 23:31
 */
@Service("UserServerService")
public class UserServerServiceImpl  extends ServiceImpl<UserServerMapper, UserServer> implements UserServerService {
    @Override
    public int BanUser(String username) {
        return this.baseMapper.BanUser(username);
    }

    @Override
    public IPage<List<DiskDTO>> disk(Integer current, Integer size, String search,Integer level,Integer groupId) {
        IPage<DiskDTO> page = new Page<>(current,size);
        return this.baseMapper.disk(page,search,level,groupId);
    }

    @Override
    public List<String> diskUser(Integer uid,String username) {
        Map<String,Object> map= this.baseMapper.getUserServerAll(username,null);
        SSHLinux sshLinux = new SSHLinux(map);
        List<String> result = sshLinux.execute("quota -uvs "+map.get("server_username"));
        sshLinux.close();
        return result;
    }

    @Override
    public boolean updateDisk(Integer uid, String username,String disk) {
        try {
            Map<String,Object> map= this.baseMapper.getUserServerAll(username,null);
            if(map!=null&&!"root".equals(map.get("server_username"))){
                String temp = "temp" + disk;
                SSHLinux sshLinux = new SSHLinux(map);
                sshLinux.execute("edquota -p "+temp+" "+map.get("server_username"));
                sshLinux.close();
                LambdaUpdateWrapper<UserServer> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper
                        .set(UserServer::getDisk,disk)
                        .eq(UserServer::getUid,uid);
                this.baseMapper.update(null,updateWrapper);
                return true;
            }
            return false;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
