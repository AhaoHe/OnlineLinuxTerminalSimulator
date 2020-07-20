package hmh.terminal.linux.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/3/17 0:34
 */
@Data
public class UserVo {
    private String username;
    private String password;
    private String nickname;
    private int sex;
    private String mail;
    private String tel;
    private String verCode;//验证码，或者==update修改，insert=新增
}
