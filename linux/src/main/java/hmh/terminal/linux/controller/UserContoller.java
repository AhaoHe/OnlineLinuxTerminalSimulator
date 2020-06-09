package hmh.terminal.linux.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import hmh.terminal.linux.config.auth.JSONAuthentication;
import hmh.terminal.linux.dao.entity.*;
import hmh.terminal.linux.dto.MenuDTO;
import hmh.terminal.linux.result.JsonResult;
import hmh.terminal.linux.result.ResultFactory;
import hmh.terminal.linux.service.*;
import hmh.terminal.linux.service.email.EMailService;
import hmh.terminal.linux.utils.BCryptPasswordEncoderUtil;
import hmh.terminal.linux.vo.UserInfoVo;
import hmh.terminal.linux.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/3/14 20:54
 * @description 登录接口
 */
@RestController
@RequestMapping("user")
@Slf4j
public class UserContoller extends JSONAuthentication {
    @Autowired
    UserService userService;
    @Autowired
    MenuService menuService;
    @Autowired
    RoleService roleService;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    EMailService emailService;
    @Autowired
    GroupService groupService;
    @Autowired
    UserServerService userServerService;
    @Autowired
    BCryptPasswordEncoderUtil bCryptPasswordEncoderUtil;
    /**
     * 查询所有数据集
     * @return
     */
    @GetMapping("AllUser")
    public JsonResult getList(@RequestParam("current") int current,
                            @RequestParam("size") int size) {
        try{
            Map<String,Object> map = new HashMap<>();
            map.put("users",userService.findAllUser(current,size));
            map.put("rolefilter",roleService.getRolesFilter());
            return  ResultFactory.SuccessResult(map);
        }catch (Exception e){
            return  ResultFactory.FailResult(e.getMessage());
        }
    }

    /*
    * 条件查询用户
    * */
    @GetMapping("userInfo")
    public JsonResult getListInfo(@RequestParam("userInfo")String userInfoVo) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(userInfoVo);
            UserInfoVo info = (UserInfoVo) JSONObject.toJavaObject(jsonObject, UserInfoVo.class);
            return ResultFactory.SuccessResult(userService.findUserBy(info));
        }catch (Exception e){
            e.printStackTrace();
            return  ResultFactory.FailResult(e.getMessage());
        }
    }


    /**
     * 查询类似username的数据集(已废除)
     * @return
     */
    @GetMapping("search")
    public JsonResult search(@RequestParam("username") String username,@RequestParam("current") int current,
                            @RequestParam("size") int size) {
        try {
            return ResultFactory.SuccessResult(userService.findUserLikeUsername(username, current, size));
        }catch (Exception e){
            return  ResultFactory.FailResult(e.getMessage());
        }
    }

    /**
     * 修改状态/删除用户
     * @return
     */
    @PutMapping("state")
    public JsonResult updateState(@RequestParam("username") String username,
                                  @RequestParam("state") Integer state) {
        try{
            return  ResultFactory.SuccessResult(userService.updateState(username,state));
        }catch (Exception e){
            return  ResultFactory.FailResult(e.getMessage());
        }
    }
    @PutMapping("states")
    public JsonResult updateStates(@RequestParam("usernames") String usernames,
                                  @RequestParam("state") Integer state) {
        try {
            String[] infos = usernames.split(",");
            return  ResultFactory.SuccessResult(userService.updateStates(infos,state));
        }catch (Exception e){
            return  ResultFactory.FailResult(e.getMessage());
        }
    }

    /**
     * 添加用户、用户自行注册。
     * @return
     */
    @PostMapping("register")
    public JsonResult register(@RequestParam("username") String username
            ,@RequestParam("password") String password
            ,@RequestParam("nickname") String nickname
            ,@RequestParam("sex") int sex
            ,@RequestParam("mail") String mail
            ,@RequestParam("tel") String tel
            ,@RequestParam("verCode") String verCode) {
        try {
            UserVo userVo = new UserVo();
            userVo.setUsername(username);
            userVo.setPassword(password);
            userVo.setNickname(nickname);
            userVo.setSex(sex);
            userVo.setMail(mail);
            userVo.setTel(tel);
            userVo.setVerCode(verCode);
            System.out.println("registerVo = " + userVo);
            log.info("新用户注册信息："+userVo);
            /*JsonResult result =*/
            emailService.validateMailVerCode(userVo.getVerCode());
            /*if(result.getCode() != 200){
                return result;
            }*/
            return  ResultFactory.SuccessResult(userService.register(userVo));
        }catch (Exception e){
            return ResultFactory.FailResult(e.getMessage());
        }
    }

    /**
     * 添加用户
     * @return
     */
    @PostMapping("info")
    public JsonResult AddUser(@RequestParam("userVo") String userVo){
        try {
            JSONObject jsonObject=JSONObject.parseObject(userVo);
            UserVo info=(UserVo) JSONObject.toJavaObject(jsonObject, UserVo.class);
            log.info("新添加用户信息："+info);
            return  ResultFactory.SuccessResult(userService.register(info));
        }catch (Exception e){
            return ResultFactory.FailResult(e.getMessage());
        }
    }

    /**
     * 查询用户信息
     * @param username
     * @return
     */
    @GetMapping("info")
    public JsonResult info(@RequestParam("username") String username) {
        try {
            //List<Menu> menus = menuService.getMenusByUserNameOld(username);
            List<Map<String,Object>> menus = menuService.buildMenusByUsername(username);
            Map<String,Object> roles = roleService.getPermissionByUserName(username);
            Map<String,Object> data = new HashMap<>(2);
            data.put("menus", menus);
            data.put("roles", roles.get("role_name"));
            data.put("level", roles.get("level"));
            data.put("groupId", roles.get("group_id"));
            data.put("gname", roles.get("gname"));
            data.put("serverId", roles.get("serverId"));
            log.info(username+"["+roles+"]"+"获取菜单："+menus);
            return  ResultFactory.SuccessResult(data);
        }catch (Exception e){
            return ResultFactory.FailResult(e.getMessage());
        }
    }

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    @PutMapping("info")
    public JsonResult update(@RequestParam("userVo") String user) {
        try {
            //List<Menu> menus = menuService.getMenusByUserNameOld(username);
            JSONObject jsonObject=JSONObject.parseObject(user);
            User info=(User) JSONObject.toJavaObject(jsonObject, User.class);
            if("true".equals(info.getPassword())){
                info.setPassword(bCryptPasswordEncoderUtil.encode("123456"));
            }else {
                info.setPassword(null);
            }
            log.info("修改用户信息："+info);
            return  ResultFactory.SuccessResult(userService.updateUser(info));
        }catch (Exception e){
            return ResultFactory.FailResult(e.getMessage());
        }
    }


    /*
    * 修改用户角色
    * */
    @PutMapping("role")
    public JsonResult update(@RequestParam("roleId") Integer roleId,@RequestParam("uid") Integer uid,@RequestParam("groupId") Integer groupId) {
        try {
            Role role= roleService.getById(roleId);
            LambdaQueryWrapper<UserServer> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserServer::getUid,uid);
            UserServer userServer = userServerService.list(queryWrapper).get(0);
            if(!groupService.RoleGroup(groupId,role.getLevel(),userServer.getServerUsername())){
                return ResultFactory.FailResult("修改失败");
            }
            LambdaUpdateWrapper<UserRole> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper
                    .set(UserRole::getRoleId,roleId)
                    .eq(UserRole::getUid,uid);
            return  ResultFactory.SuccessResult(userRoleService.update(updateWrapper));
        }catch (Exception e){
            return ResultFactory.FailResult("修改失败");
        }
    }



    /*
    * 验证邮箱是否已经注册
    * */
    @GetMapping("validateMail")
    public JsonResult validateMail(@RequestParam("mail") String mail) {
        try {
            return  ResultFactory.SuccessResult(userService.validateMail(mail));
        }catch (Exception e){
            return ResultFactory.FailResult(e.getMessage());
        }
    }
    /*
    * 验证用户名是否已经注册
    * */
    @GetMapping("validateUsername")
    public JsonResult validateUsername(@RequestParam("username") String username) {
        try {
            User user =userService.getUserByUserName(username);
            if(user ==null)
                return  ResultFactory.SuccessResult(true);
            else {
                return ResultFactory.FailResult("账号已存在");
            }
        }catch (Exception e){
            return ResultFactory.FailResult(e.getMessage());
        }
    }


    /*
    * 发送验证邮件，发送验证码
    * */
    @PostMapping("/email")
    public JsonResult sendEmail(@RequestParam("emailAddress") String emailAddress) {
        try {
            emailService.sendEmailVerCode(emailAddress);
            return ResultFactory.SuccessResult("邮件发送成功");
        } catch (Exception e) {
            return ResultFactory.FailResult("邮件发送失败");
        }
    }

}
