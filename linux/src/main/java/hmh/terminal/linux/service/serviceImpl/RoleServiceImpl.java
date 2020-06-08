package hmh.terminal.linux.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hmh.terminal.linux.dao.entity.Role;
import hmh.terminal.linux.dao.mapper.role.RoleMapper;
import hmh.terminal.linux.dto.RoleFilterDTO;
import hmh.terminal.linux.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/3/17 16:29
 */
@Service("RoleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    /**
     * 根据用户名称查询角色
     * @param userName
     * @return
     */
    @Override
    public List<String> getRolesByUserName(String userName){
        return this.baseMapper.getRolesByUserName(userName);
    }

    @Override
    public Map<String, Object> getPermissionByUserName(String userName) {
        return this.baseMapper.getPermissionByUserName(userName);
    }

    /*
     * 获取角色过滤器
     * */
    @Override
    public List<RoleFilterDTO> getRolesFilter() {
        return this.baseMapper.getRoleFilter();
    }
}
