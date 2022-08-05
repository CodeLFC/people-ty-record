package gaozhi.online.peoplety.record.service.feign;

import gaozhi.online.peoplety.config.FeignConfiguration;
import gaozhi.online.peoplety.entity.dto.IPInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author http://gaozhi.online
 * @version 1.0
 * @description: TODO ip138服务  访问异常
 * @date 2022/8/5 15:14
 */
@Deprecated
@FeignClient(value = "ip138FeignClient",url = "http://api.ip138.com", configuration = FeignConfiguration.class)
public interface IP138FeignClient {
    @GetMapping("/ip/")
    IPInfoDTO getIPInfo(@RequestParam("token") String token, @RequestParam("datatype") String datatype, @RequestParam("ip") String ip);
}
