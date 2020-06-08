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
 * @date 2020/5/5 18:19
 */
@Data
@TableName(value = "command")
public class Command extends Model<Command> {
    @TableId(value = "id",type = IdType.AUTO)
    private int id;
    @TableField(value = "command")
    private String command;
    @TableField(value = "description")
    private String description;
    @TableField(value = "status",select = false)
    private int status;
}
