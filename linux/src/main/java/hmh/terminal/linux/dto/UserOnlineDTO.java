package hmh.terminal.linux.dto;

import lombok.Data;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/6/8 20:37
 */
@Data
public class UserOnlineDTO {
    private String username;
    private Integer count;
    private Integer dangercount;
    private Integer countNow;
    private Integer dangercountNow;
    private String gname;
    private String groupName;
    private Integer groupId;
    private Integer serverId;
    private Integer level;
    private Integer power;
    private boolean online;
    private boolean disabled;
}
