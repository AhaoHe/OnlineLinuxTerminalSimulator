package hmh.terminal.linux.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/4/27 16:14
 */
@Data
@TableName(value = "menu")
public class MenuDTO {
    @TableId(value = "id", type = IdType.AUTO)
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
    @TableField(value = "menu_sort")
    private int menuSort;
    @TableField(value = "affix")
    private boolean affix;
    @TableField(value = "hidden")
    private boolean hidden;
    @TableField(select = false)
    private List<MenuDTO> menuDTOS;
}
