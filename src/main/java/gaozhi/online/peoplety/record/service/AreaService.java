package gaozhi.online.peoplety.record.service;

import gaozhi.online.peoplety.record.config.IP138Config;
import gaozhi.online.peoplety.record.entity.Area;
import gaozhi.online.peoplety.record.entity.IPInfo;
import gaozhi.online.peoplety.record.mapper.AreaMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO  地区服务
 * @date 2022/5/14 12:04
 */
@Service
@Slf4j
public class AreaService {
    @Resource
    private AreaMapper areaMapper;

    public List<Area> getAreas() {
        return areaMapper.getAll();
    }

    @Autowired
    private IP138Config ip138Config;
    private final RestTemplate restTemplate = new RestTemplate();
    private final Map<String, IPInfo> infoMap = new HashMap<>();
    //有效期
    private final long VALIDATE_TIME = 30 * 24 * 60 * 60 * 1000L;
    private long validateTime;

    /**
     * @description: 获取ip信息
     * @param: ip
     * @return: gaozhi.online.peoplety.record.entity.IPInfo
     * @author LiFucheng
     * @date: 2022/5/30 16:07
     */
    public IPInfo getIpInfo(String ip) {
        //每一个月刷新一次缓存，最终通过数据库实现，暂时使用map代替数据库
        if (System.currentTimeMillis() - validateTime > VALIDATE_TIME) {
            infoMap.clear();
            validateTime = System.currentTimeMillis();
        }
        if (infoMap.containsKey(ip)) {
            log.info("catch ip info:{}", infoMap.get(ip));
            return infoMap.get(ip);
        }

        Map<String, String> params = new HashMap<>();
        params.put("token", ip138Config.getToken());
        params.put("datatype", ip138Config.getDataType());
        params.put("ip", ip);
        //远程调用,
        IPInfo info = restTemplate.getForObject(ip138Config.getUrl(), IPInfo.class, params);
        if (info != null && "ok".equals(info.getRet())) {
            infoMap.put(ip, info);
            log.warn("ip info:{}", info);
        }
        return info;
    }
}
