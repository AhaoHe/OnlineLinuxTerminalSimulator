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
 * @date 2020/3/18 1:18
 */
@Data
@TableName("role_api")
@SuppressWarnings("serial")
public class RoleApi extends Model<RoleApi> {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField(value = "role_id")
    private Integer roleId;
    @TableField(value = "api_id")
    private Integer apiId;


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
