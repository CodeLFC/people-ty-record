package gaozhi.online.peoplety.record.service;

import gaozhi.online.base.exception.BusinessRuntimeException;
import gaozhi.online.base.exception.enums.ServerExceptionEnum;
import gaozhi.online.peoplety.entity.dto.IPInfoDTO;
import gaozhi.online.peoplety.entity.factory.IPInfoBeanCopyFactory;
import gaozhi.online.peoplety.record.config.IP138Config;
import gaozhi.online.peoplety.entity.*;
import gaozhi.online.peoplety.record.mapper.AreaMapper;
import gaozhi.online.peoplety.record.mapper.IPInfoMapper;
import gaozhi.online.peoplety.record.service.feign.IP138FeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Resource
    private IP138FeignClient ip138FeignClient;

    private final RestTemplate restTemplate = new RestTemplate();
    /**
     * @description: 有效期
     * @author http://gaozhi.online
     * @date: 2022/8/5 15:23
     */
    private final long VALIDATE_TIME = 24 * 30 * 24 * 60 * 60 * 1000L;

    private final IPInfoBeanCopyFactory.DO2DTO ipInfoDO2DTO = new IPInfoBeanCopyFactory.DO2DTO();
    private final IPInfoBeanCopyFactory.DTO2DO ipInfoDTO2DO = new IPInfoBeanCopyFactory.DTO2DO();

    /**
     * @description: 获取ip信息
     * @param: ip
     * @return: gaozhi.online.peoplety.record.entity.IPInfo
     * @author LiFucheng
     * @date: 2022/5/30 16:07
     */
    @Transactional
    public IPInfoDTO getIpInfo(String ip) {
        IPInfo info = ipInfoMapper.selectByIP(ip);
        //每一个月刷新一次缓存,暂时使用map代替数据库
        if (info != null && System.currentTimeMillis() - info.getTime() < VALIDATE_TIME && info.getData() != null) {
            return ipInfoDO2DTO.copy(info, IPInfoDTO.class);
        }
        //远程过程调用时间很长，如果为空需要提前插入
        if (info == null) {
            info = new IPInfo();
            info.setIp(ip);
            ipInfoMapper.insert(info);
        }

        Map<String, String> params = new HashMap<>();
        params.put("token", ip138Config.getToken());
        params.put("datatype", ip138Config.getDataType());
        params.put("ip", ip);
        IPInfoDTO infoDTO = restTemplate.getForObject("https://api.ip138.com/ip/?ip={ip}&datatype={datatype}&token={token}", IPInfoDTO.class, params);
        //远程调用,
        // IPInfoDTO infoDTO = ip138FeignClient.getIPInfo(ip138Config.getToken(), ip138Config.getDataType(), ip);
        if (infoDTO == null || !"ok".equals(infoDTO.getRet())) {
            throw new BusinessRuntimeException(ServerExceptionEnum.GENERAL_ERROR, "ip138远程服务调用异常：" + infoDTO);
        }
        //更新信息
        info.setTime(System.currentTimeMillis());
        ipInfoDTO2DO.copy(infoDTO, info);
        ipInfoMapper.updateIPInfo(info);
        log.info("远程调用更新IP地址{}", info);
        return infoDTO;
    }
}
