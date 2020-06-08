package hmh.terminal.linux.vo;

import hmh.terminal.linux.dao.entity.Api;
import hmh.terminal.linux.dao.entity.Menu;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/5/24 16:19
 */
public class ApiDataVo extends Api {
    private String parentPath;

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }


}
