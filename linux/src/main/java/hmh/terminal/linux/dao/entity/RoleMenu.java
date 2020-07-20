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
 * @date 2020/3/17 1:10
 */
@Data
@TableName(value = "role_menu")
@SuppressWarnings("serial")
public class RoleMenu extends Model<RoleMenu> {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField(value = "role_id")
    private int roleId;
    @TableField(value = "menu_id")
    private int menuId;

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
