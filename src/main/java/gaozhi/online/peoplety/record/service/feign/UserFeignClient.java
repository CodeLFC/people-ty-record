package gaozhi.online.peoplety.record.service.feign;

import feign.HeaderMap;
import feign.RequestLine;
import gaozhi.online.base.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import java.util.Map;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 用户服务
 * @date 2022/7/29 19:08
 */
@FeignClient(value = "app-peoplety-user", configuration = FeignConfiguration.class)
public interface UserFeignClient {

    @RequestLine("POST /general/user/post/check_auth")
    Result checkAuth(@HeaderMap Map<String,String> headers);
}
