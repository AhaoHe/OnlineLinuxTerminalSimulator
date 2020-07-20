package hmh.terminal.linux.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/5/17 16:10
 */
@Data
@TableName("role")
public class RoleFilterDTO {
    @TableId(value = "role_id",type = IdType.AUTO)
    private int value;
    @TableField(value = "role_name")
    private String text;
}
