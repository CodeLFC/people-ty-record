package gaozhi.online.peoplety.record.checker;

import com.google.gson.Gson;
import gaozhi.online.base.exception.BusinessRuntimeException;
import gaozhi.online.base.interceptor.HeaderPropertyChecker;
import gaozhi.online.base.result.Result;
import gaozhi.online.peoplety.record.entity.Token;
import gaozhi.online.peoplety.record.exception.UserException;
import gaozhi.online.peoplety.record.exception.enums.UserExceptionEnum;
import gaozhi.online.peoplety.record.service.UserService;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.log.LogDelegateFactory;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 检查token
 */
@Component(TokenChecker.HEADER_CHECKER_NAME)
public class TokenChecker implements HeaderPropertyChecker<Token> {
    public static final String HEADER_CHECKER_NAME = "token";
    public static final String HEADER_ATTRIBUTE_IP = "ip";
    private final Gson gson = new Gson();
    private final Log log = LogDelegateFactory.getHiddenLog(TokenChecker.class);

    @Autowired
    private UserService userService;

    @Override
    public Token check(String value, String url, String ip,HttpServletRequest request, HttpServletResponse response) {
        //log.info("url=" + url + " 检查用户token:" + value);
        Result result = userService.checkToken(value, url, ip);
        if (result.getCode() != Result.SUCCESSResultEnum.SUCCESS.code()) {
            throw new UserException(UserExceptionEnum.USER_AUTH_ERROR);
        }
        request.setAttribute(HEADER_ATTRIBUTE_IP, ip);
        return gson.fromJson(value, Token.class);
    }

}
