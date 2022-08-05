package gaozhi.online.peoplety.record.service.log;

import gaozhi.online.base.log.ILogService;
import gaozhi.online.base.log.SysLog;
import gaozhi.online.base.result.Result;
import gaozhi.online.peoplety.config.SystemPropertyConfig;
import gaozhi.online.peoplety.feign.SysLogFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author http://gaozhi.online
 * @version 1.0
 * @description: TODO  控制层日志
 * @date 2022/8/4 16:16
 */
@Service
@Slf4j
public class ControllerLogServiceImpl implements ILogService {
    @Autowired
    private SystemPropertyConfig systemPropertyConfig;
    @Resource
    private SysLogFeignClient sysLogFeignClient;

    @Override
    public void handle(SysLog log) {
        log.setType(systemPropertyConfig.getApplicationName());
        Result result = sysLogFeignClient.writeLog(log);
    }
}
