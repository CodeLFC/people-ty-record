package gaozhi.online.peoplety.record.exception;
import gaozhi.online.base.exception.BusinessRuntimeException;
import gaozhi.online.base.result.Result;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 用户异常
 * @date 2022/3/4 18:42
 */
public class UserException extends BusinessRuntimeException {

    public UserException(Result.ResultEnum exception) {
        super(exception);
    }

    public UserException(Result.ResultEnum exception, String msg) {
        super(exception, msg);
    }
}
