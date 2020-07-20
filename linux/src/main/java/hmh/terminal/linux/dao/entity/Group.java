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
 * @date 2020/5/10 21:56
 */
@Data
@TableName(value = "sys_group")
public class Group extends Model<Group> {
    @TableId(value = "id",type = IdType.AUTO)
    private int id;
    @TableField(value = "group_name")
    private String groupName;
    @TableField(value = "name")
    private String name;
    @TableField(value = "serverId")
    private Integer serverId;
    @TableField(value = "isFilter")
    private boolean filter;
    @TableField(value = "status")
    private int status;
}
