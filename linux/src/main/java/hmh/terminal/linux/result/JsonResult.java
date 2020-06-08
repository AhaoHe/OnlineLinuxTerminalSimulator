package hmh.terminal.linux.result;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/3/14 20:50
 */
/**
 * 通用返回值实例
 */
public class JsonResult {
    /**
     * 响应状态码
     */
    private int code;
    /**
     * 响应状态消息
     */
    private String msg;
    /**
     * 响应结果对象
     */
    private Object data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public Object getObject() {
        return data;
    }

    public void setObject(Object object) {
        this.data = object;
    }

    JsonResult(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
