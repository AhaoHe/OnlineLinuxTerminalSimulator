package hmh.terminal.linux.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/1/12 1:41
 */
@Data
@TableName(value = "server")
public class Server {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField(value = "hostname")
    private String hostname;
    @TableField(value = "port")
    private Integer port;
    @TableField(value = "rootPassword")
    private String rootPassword;
    @TableField(value = "sqlkey")
    private String sqlkey;
    @TableField(value = "filekey")
    private String fileKey;
    @TableField(value = "status")
    private Integer status;
    @TableField(value = "disk")
    private String disk;
    @TableField(value = "register_enable")
    private Integer registerEnable;
    @TableField(exist = false)
    private boolean enable;
}
