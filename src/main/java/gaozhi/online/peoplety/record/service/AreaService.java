package gaozhi.online.peoplety.record.service;

import gaozhi.online.peoplety.record.entity.Area;
import gaozhi.online.peoplety.record.mapper.AreaMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO  地区服务
 * @date 2022/5/14 12:04
 */
@Service
public class AreaService {
    @Resource
    private AreaMapper areaMapper;

    public List<Area> getAreas() {
        return areaMapper.getAll();
    }
}
