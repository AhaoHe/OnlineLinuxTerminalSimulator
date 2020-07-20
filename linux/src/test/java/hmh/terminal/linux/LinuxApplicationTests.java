package hmh.terminal.linux;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import hmh.terminal.linux.dao.entity.*;
import hmh.terminal.linux.dao.mapper.group.GroupMapper;
import hmh.terminal.linux.dao.mapper.server.ServerMapper;
import hmh.terminal.linux.dao.mapper.user.UserMapper;
import hmh.terminal.linux.dao.mapper.user_server.UserServerMapper;
import hmh.terminal.linux.service.MenuService;
import hmh.terminal.linux.service.SSHService;
import hmh.terminal.linux.service.UserService;
import hmh.terminal.linux.utils.BCryptPasswordEncoderUtil;
import org.apache.ibatis.annotations.Update;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LinuxApplicationTests {

    @Autowired
    private ServerMapper serverMapper;
    @Autowired
    private UserServerMapper userServerMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SSHService sshService;
    @Autowired
    BCryptPasswordEncoderUtil bCryptPasswordEncoderUtil;

    @Test
    void contextLoads() {
    }

    @Test
    void getList(){//learn about mybatis-plus
        List<Server> serverList = serverMapper.selectList(null);
        serverList.forEach(System.out::println);
    }

    @Test
    void setPasswordByUserName(){
        User user = userService.getUserByUserName("admin");
        user.setPassword(bCryptPasswordEncoderUtil.encode("admin"));
        System.out.println(user);
        userMapper.updateById(user);
    }

    @Test
    void getMenusByUserName(){
        List<Menu> menus = menuService.getMenusByUserName("admin");
        System.out.println(menus);
    }
    @Test
    void getGroup(){
        LambdaQueryWrapper<Group> queryGroup = new LambdaQueryWrapper<>();
        queryGroup
                .eq(Group::getStatus,0);
        Group group = groupMapper.selectOne(queryGroup);
        System.out.println(group);
    }
    @Test
    void getUser(){
        User user = new User();
        user.setPassword("123");
        user.setUserName("test");
        user.setMail("123@qq.com");
        user.setSex(2);
        userMapper.insert(user);
        System.out.println(user.getUid());
    }

}
