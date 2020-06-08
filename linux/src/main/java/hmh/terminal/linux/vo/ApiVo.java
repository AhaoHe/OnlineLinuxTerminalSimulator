package hmh.terminal.linux.vo;

import hmh.terminal.linux.dao.entity.Api;
import lombok.Data;

import java.util.List;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/3/17 1:35
 */
@Data
public class ApiVo {
    private int id;
    private String label;
    private int pid;
    private int sort;
    private String url;
    private boolean newData;
    private List<ApiVo> children;
}
