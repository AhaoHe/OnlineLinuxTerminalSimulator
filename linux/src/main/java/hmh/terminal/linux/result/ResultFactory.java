package hmh.terminal.linux.result;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/3/14 20:50
 * @description 响应结果生成工厂类
 */
public class ResultFactory {
    public static JsonResult SuccessResult(Object data) {
        return buildResult(ResultCode.SUCCESS, "成功", data);
    }

    public static JsonResult FailResult(String message) {
        return buildResult(ResultCode.FAIL, message, null);
    }

    public static JsonResult UnAuthorizedResult(String message) {
        return buildResult(ResultCode.UNAUTHORIZED, message, null);
    }

    private static JsonResult buildResult(ResultCode resultCode, String message, Object data) {
        return buildResult(resultCode.code, message, data);
    }

    private static JsonResult buildResult(int resultCode, String message, Object data) {
        return new JsonResult(resultCode, message, data);
    }
}
