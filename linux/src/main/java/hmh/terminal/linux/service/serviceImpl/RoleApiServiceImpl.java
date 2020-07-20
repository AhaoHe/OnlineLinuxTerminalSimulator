package hmh.terminal.linux.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hmh.terminal.linux.dao.entity.RoleApi;
import hmh.terminal.linux.dao.entity.RoleMenu;
import hmh.terminal.linux.dao.mapper.role_api.RoleApiMapper;
import hmh.terminal.linux.dao.mapper.role_menu.RoleMenuMapper;
import hmh.terminal.linux.service.RoleApiService;
import hmh.terminal.linux.service.RoleMenuService;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/5/24 15:27
 */
@Service("RoleApiService")
public class RoleApiServiceImpl extends ServiceImpl<RoleApiMapper, RoleApi> implements RoleApiService {

    @Override
    public int DelRoleApi(List<Integer> info){
        LambdaUpdateWrapper<RoleApi> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(RoleApi::getApiId,info);
        return this.baseMapper.delete(updateWrapper);
    }

    @Override
    public boolean SaveRoleApi(Integer roleId, int[] apiIds) {
        LambdaQueryWrapper<RoleApi> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleApi::getRoleId,roleId);
        this.baseMapper.delete(queryWrapper);
        List<RoleApi> roleApis = new LinkedList<>();
        for (Integer apiId : apiIds) {
            RoleApi roleApi = new RoleApi();
            roleApi.setRoleId(roleId);
            roleApi.setApiId(apiId);
            roleApis.add(roleApi);
        }
        return this.saveBatch(roleApis);
    }

}
