package hmh.terminal.linux.dto;

import lombok.Data;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/6/7 12:02
 */
@Data
public class DiskDTO {
    private Integer uid;
    private String username;
    private String disk;
    private Integer serverId;
    private String gname;
    private Integer serverStatus;
}
