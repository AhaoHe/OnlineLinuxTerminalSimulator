package hmh.terminal.linux.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/5/16 17:03
 */
@Data
public class ManageUserDTO {
    private  int uid;
    private String username;
    private String nickname;
    private String password;
    private String sex;
    private String mail;
    private String tel;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String lastTime;
    private int state;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private String createTime;
    private int roleId;
    private String roleName;
    private int level;
    private String server;
    private Integer serverId;
    private String serverUsername;
    private String gname;
    private Integer groupId;
}
