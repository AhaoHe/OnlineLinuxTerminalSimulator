package hmh.terminal.linux.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/2/2 13:26
 */
@Data
@TableName(value = "user_server")
public class UserServer {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField(value = "serverId")
    private Integer serverId;
    @TableField(value = "uid")
    private Integer uid;
    @TableField(value = "server_username")
    private String serverUsername;
    @TableField(value = "status")
    private boolean status;
    @TableField(value = "disk")
    private String disk;
    @TableField(value = "group_id")
    private int groupId;
}
