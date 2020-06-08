package hmh.terminal.linux.service.auth;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/3/17 16:07
 */

import hmh.terminal.linux.dao.entity.User;
import hmh.terminal.linux.service.RoleService;
import hmh.terminal.linux.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 要实现UserDetailsService接口，这个接口是security提供的
 */
@Service
public class AuthUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    //用户激活状态  3为正常用户
    private static final int USER_STATE = 3;
    //黑名单为0
    private static final int USER_BLACKLIST = 0;

    /**
     * 通过账号查找用户、角色的信息
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserByUserName(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("%s.这个用户不存在", username));
        }else if(user.getState()==USER_BLACKLIST){
            throw new UsernameNotFoundException(String.format("%s.已被加入黑名单", username));
        }
        else {
            //查找角色
            /*List<String> roles =  roleService.getRolesByUserName(username);
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            for (String role : roles) {
                authorities.add(new SimpleGrantedAuthority(role));
            }*/
            System.out.println("loadUserByUsername......user ===> " + user);
            return new AuthUser(user.getUserName(), user.getPassword(), user.getState(), null);
        }
    }
}