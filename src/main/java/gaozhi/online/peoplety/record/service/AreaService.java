package gaozhi.online.peoplety.record.service;

import gaozhi.online.base.exception.BusinessRuntimeException;
import gaozhi.online.base.exception.enums.ServerExceptionEnum;
import gaozhi.online.peoplety.record.config.IP138Config;
import gaozhi.online.peoplety.record.entity.Area;
import gaozhi.online.peoplety.record.entity.IPInfo;
import gaozhi.online.peoplety.record.entity.IPInfoDB;
import gaozhi.online.peoplety.record.mapper.AreaMapper;
import gaozhi.online.peoplety.record.mapper.IPInfoMapper;
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
    @Resource
    private IPInfoMapper ipInfoMapper;

    private final RestTemplate restTemplate = new RestTemplate();
    //有效期
    private final long VALIDATE_TIME = 12 * 30 * 24 * 60 * 60 * 1000L;

    /**
     * @description: 获取ip信息
     * @param: ip
     * @return: gaozhi.online.peoplety.record.entity.IPInfo
     * @author LiFucheng
     * @date: 2022/5/30 16:07
     */
    public IPInfo getIpInfo(String ip) {
        IPInfoDB infoDB = ipInfoMapper.selectByIP(ip);
        //每一个月刷新一次缓存,暂时使用map代替数据库
        if (infoDB != null && System.currentTimeMillis() - infoDB.getTime() < VALIDATE_TIME) {
            //  log.info("catch ip info:{}", infoDB);
            return infoDB.getIPInfo();
        }
        //远程过程调用时间很长，如果为空需要提前插入
        if (infoDB == null) {
            infoDB = new IPInfoDB();
            infoDB.setIp(ip);
            ipInfoMapper.insert(infoDB);
        }

        Map<String, String> params = new HashMap<>();
        params.put("token", ip138Config.getToken());
        params.put("datatype", ip138Config.getDataType());
        params.put("ip", ip);

        //远程调用,
        IPInfo info = restTemplate.getForObject(ip138Config.getUrl(), IPInfo.class, params);
        if (info == null || !"ok".equals(info.getRet())) {
            throw new BusinessRuntimeException(ServerExceptionEnum.GENERAL_ERROR, "ip138远程服务调用异常：" + info);
        }
        //更新信息
        infoDB.setTime(System.currentTimeMillis());
        infoDB.setIPInfo(info);
        ipInfoMapper.updateIPInfo(infoDB);
        return info;
    }
}
