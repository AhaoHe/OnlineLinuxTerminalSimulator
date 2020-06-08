package hmh.terminal.linux.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import org.thymeleaf.model.IModel;

import java.util.Date;
import java.util.List;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/1/12 1:41
 */
@Data
@TableName(value = "user")
public class User extends Model<User> {
    @TableId(value = "uid",type = IdType.AUTO)
    private Integer uid;
    @TableField(value = "username")
    private String userName;
    @TableField(value = "password")
    private String password;
    @TableField(value = "nickname")
    private String nickname;
    @TableField(value = "sex")
    private int sex;
    @TableField(value = "mail")
    private String mail;
    @TableField(value = "tel")
    private String tel;
    @TableField(value = "last_time")
    private Date lastTime;
    @TableField(value = "description")
    private String description;
    @TableField(value = "state")
    private int state;
    @TableField(value = "create_time")
    private Date createTime;
    @TableField(value = "update_time")
    private Date updateTime;
    @TableField(value = "update_user")
    private String updateUser;

    public User(){}

    public User(Integer uid, String userName, String password, String nickname, String mail, String tel, Date lastTime, String description, int state, Date createTime, Date updateTime, String updateUser) {
        this.uid = uid;
        this.userName = userName;
        this.password = password;
        this.nickname = nickname;
        this.mail = mail;
        this.tel = tel;
        this.lastTime = lastTime;
        this.description = description;
        this.state = state;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.updateUser = updateUser;
    }
}
