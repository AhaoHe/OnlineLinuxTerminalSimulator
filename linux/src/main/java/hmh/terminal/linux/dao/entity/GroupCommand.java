package hmh.terminal.linux.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/6/7 21:18
 */
@Data
@TableName(value = "group_command")
public class GroupCommand extends Model<hmh.terminal.linux.dao.entity.GroupCommand>{
    @TableField(value = "group_id")
    private Integer groupId;
    @TableField(value = "command_id")
    private Integer commandId;

}
