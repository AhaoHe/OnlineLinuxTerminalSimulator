package hmh.terminal.linux.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/6/9 2:26
 */
@Data
@TableName("user_command")
public class UserCommand {
    @TableId(value = "id",type = IdType.AUTO)
    private  Integer id;
    private String username;
    @TableField("serverId")
    private Integer serverId;
    @TableField("user_command")
    private String command;
    private boolean danger;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private String time;
}
