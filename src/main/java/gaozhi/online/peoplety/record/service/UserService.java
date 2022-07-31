package gaozhi.online.peoplety.record.service;

import gaozhi.online.base.interceptor.HeaderChecker;
import gaozhi.online.base.result.Result;
import gaozhi.online.peoplety.record.service.feign.UserFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO
 * @date 2022/5/13 19:28
 */
@Service
@Slf4j
public class UserService {
    @Autowired
    private UserFeignClient userFeignClient;

    public Result checkToken(String token, String url, String clientIp) {
        //创建请求头
        Map<String, String> httpHeaders = new HashMap<>();
        httpHeaders.put(HeaderChecker.accessToken, token);
        httpHeaders.put(HeaderChecker.rpcURLKey, url);
        httpHeaders.put(HeaderChecker.rpcClientIp, clientIp);
        return userFeignClient.checkAuth(httpHeaders);
    }
}
