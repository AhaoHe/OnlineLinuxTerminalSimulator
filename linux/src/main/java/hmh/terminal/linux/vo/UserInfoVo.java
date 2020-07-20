package hmh.terminal.linux.vo;

import lombok.Data;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/5/19 0:30
 */
@Data
public class UserInfoVo {
    int uid;
    String search;
    String roleId;
    int dateTime;
    String order;
    int current;
    int pageSize;
    String selectTime;
    int ban;
}
