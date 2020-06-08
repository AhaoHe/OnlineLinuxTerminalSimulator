package hmh.terminal.linux.vo;

import hmh.terminal.linux.dao.entity.Command;
import lombok.Data;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/6/3 20:00
 */
@Data
public class CommandVo {
    private String search;
    private int pageSize;
    private int current;

}
