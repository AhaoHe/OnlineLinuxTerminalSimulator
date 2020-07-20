package hmh.terminal.linux.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/3/17 0:44
 */
@Data
@TableName(value = "role")
@SuppressWarnings("serial")
public class Role extends Model<Role> {
    @TableId(value = "role_id",type = IdType.AUTO)
    private int roleId;
    @TableField(value = "role_name")
    private String roleName;
    @TableField(value = "level")
    private int level;
    @TableField(value = "description")
    private String description;

    public Role() {
    }

    public Role(int roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public Role(int roleId, String roleName, String description) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.description = description;
    }

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.roleId;
    }

}
