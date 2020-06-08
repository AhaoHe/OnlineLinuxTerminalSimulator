package hmh.terminal.linux.dao.entity;



import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/3/17 16:16
 */
@Data
@TableName("user_role")
@SuppressWarnings("serial")
public class UserRole extends Model<UserRole> {
    @TableId(value = "id",type = IdType.AUTO)
    private int id;
    @TableField(value = "role_id")
    private int roleId;
    @TableField(value = "uid")
    private int uid;


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
