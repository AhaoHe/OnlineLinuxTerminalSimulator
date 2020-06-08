package hmh.terminal.linux.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hmh.terminal.linux.dao.entity.*;
import hmh.terminal.linux.dao.mapper.group.GroupMapper;
import hmh.terminal.linux.dao.mapper.role.RoleMapper;
import hmh.terminal.linux.dao.mapper.server.ServerMapper;
import hmh.terminal.linux.dao.mapper.user.UserMapper;
import hmh.terminal.linux.dao.mapper.user_role.UserRoleMapper;
import hmh.terminal.linux.dao.mapper.user_server.UserServerMapper;
import hmh.terminal.linux.dto.ManageUserDTO;
import hmh.terminal.linux.service.UserService;
import hmh.terminal.linux.utils.BCryptPasswordEncoderUtil;
import hmh.terminal.linux.utils.SSHLinux;
import hmh.terminal.linux.vo.UserInfoVo;
import hmh.terminal.linux.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/3/17 2:22
 */
/**
 * 用户服务
 */
@Service("UserService")
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    BCryptPasswordEncoderUtil bCryptPasswordEncoderUtil;
    @Autowired
    ServerMapper serverMapper ;
    @Autowired
    GroupMapper groupMapper;
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    UserRoleMapper userRoleMapper;
    @Autowired
    UserServerMapper userServerMapper;

    //用户激活状态  3为正常用户
    private static final int USER_STATE = 3;
    //黑名单为0
    private static final int USER_BLACKLIST = 0;

    /*
    * 查找所有用户信息，管理员
    * */
    @Override
    public IPage<List<ManageUserDTO>> findAllUser(int current,int size) {
        IPage<ManageUserDTO> page = new Page<>(current,size);
        return this.baseMapper.findAllUser(page);
    }

    /**
     * 条件查找用户
     * @return
     */
    @Override
    public IPage<List<ManageUserDTO>> findUserBy(UserInfoVo userInfoVo) {
        IPage<ManageUserDTO> page = new Page<>(userInfoVo.getCurrent(),userInfoVo.getPageSize());
        String[] selectTime=userInfoVo.getSelectTime().split(",");
        if(selectTime.length>1){
            userInfoVo.setSelectTime(" > '"+selectTime[0]+"' and '"+selectTime[1]+"' > ");
        }
        return this.baseMapper.findUserBy(page,userInfoVo);
    }


    /*
    * 查找相似用户信息
    * */
    @Override
    public IPage<List<ManageUserDTO>> findUserLikeUsername(String username,int current,int size) {
        IPage<ManageUserDTO> page = new Page<>(current,size);
        return this.baseMapper.findUserLikeUsername(page,username);
    }

    /*
    * 修改用户信息
    * */
    @Override
    public int updateUser(User user) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<User>();
        updateWrapper
                .eq(User::getUserName,user.getUserName());
        return this.baseMapper.update(user,updateWrapper);
    }



    /*
    * 修改状态/删除用户
    * */
    @Override
    public int updateState(String username,int state) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<User>();
        updateWrapper
                .set(User::getState,state)
                .eq(User::getUserName,username);
        return this.baseMapper.update(null,updateWrapper);
    }
    @Override
    public int updateStates(String[] usernames,int state) {
        for(String username:usernames){
            LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<User>();
            updateWrapper
                    .set(User::getState,state)
                    .eq(User::getUserName,username);
            this.baseMapper.update(null,updateWrapper);
        }
        return usernames.length;
    }

    /**
     * 通过账号查询用户
     * @param username
     * @return
     */
    @Override
    public User getUserByUserName(String username) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //查询条件：全匹配账号名，和状态为3的账号
        lambdaQueryWrapper
                .eq(User::getUserName,username)
                .and(i->
                    i.eq(User::getState,USER_STATE).or()
                    .eq(User::getState,USER_BLACKLIST)
                );

        //用getOne查询一个对象出来
        User user = this.getOne(lambdaQueryWrapper);

        return  user;
    }

    /**
     * 个性化验证登录
     * @param username 账号
     * @param rawPassword 原始密码
     * @return
     */
    @Override
    public boolean checkLogin(String username,String rawPassword) throws Exception {
        User user = this.getUserByUserName(username);
        System.out.println("user = " + user);
        if (user == null) {
            //System.out.println("checkLogin--------->账号不存在，请重新尝试！");
            //设置友好提示
            throw  new Exception("账户或密码不正确！");
        }else {
            if(user.getState()==USER_BLACKLIST){
                throw new Exception("账户已被禁止登录！");
            }
            //加密的密码
            String encodedPassword = user.getPassword();
            //和加密后的密码进行比配
            if(!bCryptPasswordEncoderUtil.matches(rawPassword,encodedPassword)) {
                //System.out.println("checkLogin--------->密码不正确！");
                //设置友好提示
                throw new Exception("账户或密码不正确！");
            }else{
                //修改登录时间
                UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
                userUpdateWrapper
                        .eq("username", username)
                        .set("last_time",new Date());
                this.baseMapper.update(user,userUpdateWrapper);
                return  true;
            }
        }
    }

    /**
     * 注册
     * @param userVo
     * @return
     * @throws Exception
     */
    @Override
    public boolean register(UserVo userVo) throws Exception {
        if(userVo !=null) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper
                    .eq(User::getMail,userVo.getMail());
            User user= this.baseMapper.selectOne(queryWrapper);
            User f = this.getUserByUserName(userVo.getUsername());
            if(user != null || f!= null) {
                throw new Exception("这个用户已经存在，不能重复。");
            }
            //服务器中添加角色
            Map<String,Object> group = groupMapper.getGroupName();
            //获取注册默认的服务器
            Map<String,Object> map =serverMapper.getServerRegister();
            //获取注册默认的角色
            LambdaQueryWrapper<Role> queryRole = new LambdaQueryWrapper<>();
            queryRole
                    .select(Role::getRoleId)
                    .eq(Role::getLevel,1);
            Role role = roleMapper.selectOne(queryRole);
            //保存到数据库
            User userPo = this.voToPo(userVo);
            try {
                this.save(userPo);
            }catch (Exception e){
                e.printStackTrace();
            }
            //保存用户角色
            UserRole ur = new UserRole();
            ur.setRoleId(role.getRoleId());
            ur.setUid(userPo.getUid());
            userRoleMapper.insert(ur);

            try{
                //保存用户的服务器
                UserServer us = new UserServer();
                us.setUid(userPo.getUid());
                int id =(int)map.get("id");
                us.setServerId(id);
                us.setServerUsername("user"+userPo.getUid());
                us.setGroupId((Integer) group.get("gid"));
                us.setStatus(true);
                us.setDisk((String) map.get("disk"));
                userServerMapper.insert(us);
                map.put("ssh_username","user"+userPo.getUid());
                map.put("username","新建角色");
                SSHLinux sshLinux = new SSHLinux(map);
                sshLinux.execute("useradd -g "+group.get("group_name")+" user"+userPo.getUid());
                sshLinux.execute("edquota -p temp"+map.get("disk")+" user"+userPo.getUid());
                sshLinux.close();
                log.info("新建角色:服务器="+us+";平台="+userVo);
            }catch (Exception e){
                e.printStackTrace();
                throw e;
            }
            return  true;
        }else{
            throw new Exception("错误消息：用户对象为空！");
        }
    }

    /**
     * VO到PO的转换
     * @param vo
     * @return
     */
    public User voToPo(UserVo vo) {
        User po = new User();
        if(vo !=null) {
            po.setUserName(vo.getUsername());
            po.setNickname(vo.getNickname());
            po.setMail(vo.getMail());
            po.setTel(vo.getTel());
            po.setSex(vo.getSex());
            //对密码进行加密
            po.setPassword(bCryptPasswordEncoderUtil.encode(vo.getPassword()));
            //设置状态为3
            po.setState(USER_STATE);
            /*Date date = new Date();
            po.setLastTime(date);*/
        }
        return po;
    }

    @Override
    public boolean validateMail(String mail) throws Exception{
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getMail,mail);
        User user= this.baseMapper.selectOne(queryWrapper);
        if(user!=null){
            throw new Exception("邮箱已存在");
        }
        return true;
    }

    @Override
    public boolean checkToken(String username, String ipAddress,String token) throws UsernameNotFoundException {

        Map<String,String> map = this.baseMapper.checkToken(username);
        if (!map.get("ipaddress").equals(ipAddress)){
            throw new UsernameNotFoundException("您已在其他网络登录！请重新登录！");
        } else if (!map.get("token").equals(token)) {
            throw new UsernameNotFoundException("验证信息不是最新的或在其他地方登录！请重新登录！");
        }
        return true;
    }

}
