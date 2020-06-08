package hmh.terminal.linux.vo;

import hmh.terminal.linux.dao.entity.Menu;
import lombok.Data;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/5/24 16:19
 */
public class MenuDataVo extends Menu {
    private String parentPath;

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }


}
