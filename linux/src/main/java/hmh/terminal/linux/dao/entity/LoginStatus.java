package hmh.terminal.linux.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/6/1 21:42
 */
@Data
@TableName(value = "loginstatus")
public class LoginStatus extends Model<LoginStatus>{
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField(value = "username")
    private String username;
    @TableField(value = "loginstatus")
    private Integer loginstatus;
    @TableField(value = "token")
    private String token;
    @TableField(value = "login_time")
    private String loginTime;
    @TableField(value = "ipaddress")
    private String ipAddress;
}
