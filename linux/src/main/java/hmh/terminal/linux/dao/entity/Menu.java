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
 * @date 2020/3/17 1:04
 */
@Data
@TableName(value = "menu")
@SuppressWarnings("serial")
public class Menu extends Model<Menu> {
    @TableId(value = "id", type = IdType.UUID)
    /*@TableField(value = "id")*/
    private Integer id;
    @TableField(value = "menu_name")
    private String menuName;
    @TableField(value = "url")
    private String url;
    @TableField(value = "component")
    private String component;
    @TableField(value = "redirect")
    private String redirect;
    @TableField(value = "icon")
    private String icon;
    @TableField(value = "pid")
    private Integer pid;
    @TableField(value = "description")
    private String description;
    @TableField(value = "affix")
    private  boolean affix;
    @TableField(value = "menu_sort")
    private int menuSort;
    @TableField(value = "hidden")
    private boolean hidden;
    @TableField(value = "update_enable")
    private boolean updateEnable;

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
