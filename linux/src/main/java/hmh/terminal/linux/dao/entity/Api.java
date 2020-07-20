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
 * @date 2020/3/18 1:03
 */
@SuppressWarnings("serial")
@TableName(value = "api")
@Data
public class Api extends Model<Api> {
    @TableId(value = "api_id",type = IdType.UUID)
    private int id;
    @TableField(value = "api_name")
    private String apiName;
    @TableField(value = "api_url")
    private String apiUrl;
    @TableField(value = "api_method")
    private String apiMethod;
    @TableField(value = "pid")
    private int pid;
    @TableField(value = "api_sort")
    private int apiSort;
    @TableField(value = "description")
    private String description;
    @TableField(value = "enable")
    private boolean enable;

    public Api(){
    }

    public Api(int id, String apiName, String apiUrl, String apiMethod, int pid, int apiSort, String description) {
        this.id = id;
        this.apiName = apiName;
        this.apiUrl = apiUrl;
        this.apiMethod = apiMethod;
        this.pid = pid;
        this.apiSort = apiSort;
        this.description = description;
    }

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
