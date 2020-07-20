package hmh.terminal.linux.result;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/3/14 20:49
 */
public enum ResultCode {
    /**
     * 成功
     */
    SUCCESS(200),
    /**
     * 失败
     */
    FAIL(400),

    /*
    * 未登录
    * */
    NotLogin(2001),

    /*
    * 其他用户登入
    * */
    OtherUserLogin(2002),



    /**
     * 未认证（签名错误）
     */
    UNAUTHORIZED(401),

    /**
     * 接口不存在
     */
    NOT_FOUND(404),

    /**
     * 服务器内部错误
     */
    INTERNAL_SERVER_ERROR(500);

    public int code;

    ResultCode(int code) {
        this.code = code;
    }

}
