package gaozhi.online.peoplety.record.service;

import gaozhi.online.base.interceptor.HeaderChecker;
import gaozhi.online.base.result.Result;
import gaozhi.online.peoplety.record.checker.TokenChecker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO
 * @date 2022/5/13 19:28
 */
@Service
@Slf4j
public class UserService {
    private static final String authURL = "http://app-peoplety-user/general/user/post/check_auth";
    @Autowired
    private RestTemplate restTemplate;

    public Result checkToken(String token, String url, String clientIp) {
        //创建请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(TokenChecker.HEADER_CHECKER_NAME, token);
        httpHeaders.add(HeaderChecker.rpcURLKey, url);
        httpHeaders.add(HeaderChecker.rpcClientIp, clientIp);
        //添加到添加进去
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, httpHeaders);
        //远程调用,
        return restTemplate.postForObject(authURL, httpEntity, Result.class);
    }
    //远程过程调用会出现异常,本地测试时应采用这种方式
//    public Result checkToken(String token, String url, String clientIp) {
//        //log.info("ip:"+clientIp+" url:"+url+" token:"+token);
//       return Result.success();
//    }
}
