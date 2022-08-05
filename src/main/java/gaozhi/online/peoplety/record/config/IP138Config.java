package gaozhi.online.peoplety.record.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO ip138配置
 * @date 2022/5/30 16:30
 */
@Data
@Component
public class IP138Config {
    @Value("${ip138.dataType}")
    private String dataType;
    @Value("${ip138.token}")
    private String token;
}
