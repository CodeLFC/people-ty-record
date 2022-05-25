package gaozhi.online.peoplety.record.exception.enums;


import gaozhi.online.base.result.Result;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 用户
 * @date 2022/1/19 22:36
 */
public enum UserExceptionEnum implements Result.ResultEnum {
    USER_AUTH_ERROR(2000,"API请求权限校验失败:请重新登陆，您的账号有被盗用的风险，请及时修改密码");
    private final int  code;
    private final String message;

    UserExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
