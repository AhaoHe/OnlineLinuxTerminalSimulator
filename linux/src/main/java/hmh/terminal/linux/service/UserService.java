package hmh.terminal.linux.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import hmh.terminal.linux.dao.entity.User;
import hmh.terminal.linux.dto.ManageUserDTO;
import hmh.terminal.linux.vo.UserInfoVo;
import hmh.terminal.linux.vo.UserVo;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Map;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/3/17 2:17
 */
public interface UserService extends IService<User> {

    /**
     * 展示所有用户
     * @return
     */
    IPage<List<ManageUserDTO>> findAllUser(int current,int size);

    /**
     * 条件查找用户
     * @return
     */
    IPage<List<ManageUserDTO>> findUserBy(UserInfoVo userInfoVo);


    /*
     * 查找相似用户信息
     * */
    IPage<List<ManageUserDTO>> findUserLikeUsername(String username,int current,int size);

    /**
     * 修改用户状态/删除用户
     * @return
     */
    int updateState(String username,int state);
    int updateStates(String[] usernames,int state);

    /*
    * 修改用户
    * */
    int updateUser(User user);

    /**
     * 通过账号查询用户
     * @param username
     * @return
     */
    User getUserByUserName(String username);

    /**
     * 个性化验证登录
     * @param username 账号
     * @param rawPassword 原始密码
     * @return
     */
    boolean checkLogin(String username,String rawPassword) throws Exception;

    /**
     * 注册
     * @param userVo
     * @return
     * @throws Exception
     */
    boolean register(UserVo userVo) throws Exception;

    User voToPo(UserVo vo);

    /*
    * 验证邮箱
    * */
    boolean validateMail(String mail) throws Exception;


    boolean checkToken(String username, String ipAddress,String token)throws UsernameNotFoundException;

    IPage<List<Map<String, Object>>> getUserServerByUserName(String query);
}
