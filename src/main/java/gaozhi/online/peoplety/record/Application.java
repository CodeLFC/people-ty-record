package gaozhi.online.peoplety.record;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 记录服务
 * @date 2022/5/13 18:47
 */
@EnableScheduling
@EnableEurekaClient
@SpringBootApplication
//@EnableFeignClients(basePackages = {"gaozhi.online.peoplety.record.service.feign"})
@ComponentScan(basePackageClasses = {gaozhi.online.base.ScanClass.class, gaozhi.online.peoplety.ScanClass.class, Application.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
