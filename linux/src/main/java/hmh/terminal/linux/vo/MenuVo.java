package hmh.terminal.linux.vo;

import hmh.terminal.linux.dao.entity.Menu;
import lombok.Data;

import java.util.List;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/3/17 1:35
 */
@Data
public class MenuVo {
    private int id;
    private String label;
    private int pid;
    private int order;
    private boolean newData;
    private List<MenuVo> children;
}
