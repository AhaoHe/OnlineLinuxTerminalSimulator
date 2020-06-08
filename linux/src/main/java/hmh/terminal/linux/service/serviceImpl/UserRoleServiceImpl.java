package hmh.terminal.linux.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hmh.terminal.linux.dao.entity.Api;
import hmh.terminal.linux.dao.entity.UserRole;
import hmh.terminal.linux.dao.mapper.api.ApiMapper;
import hmh.terminal.linux.dao.mapper.user_role.UserRoleMapper;
import hmh.terminal.linux.service.ApiService;
import hmh.terminal.linux.service.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/5/31 23:16
 */
@Service("UserRoleService")
@Slf4j
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
}
