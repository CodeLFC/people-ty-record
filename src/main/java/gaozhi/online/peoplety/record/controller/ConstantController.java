package gaozhi.online.peoplety.record.controller;
import gaozhi.online.base.interceptor.HeaderChecker;
import gaozhi.online.peoplety.record.entity.Area;
import gaozhi.online.peoplety.record.entity.RecordType;
import gaozhi.online.peoplety.record.service.AreaService;
import gaozhi.online.peoplety.record.service.RecordTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 常量
 * @date 2022/5/14 11:22
 */
@Validated
@RestController
@RequestMapping("/general/constant")
public class ConstantController {
    @Autowired
    private AreaService areaService;
    @Autowired
    private RecordTypeService recordTypeService;
    /**
     * @description: 获取街道列表，此信息不轻易改变，应该每隔一天获取一次
     * @param:
     * @return: gaozhi.online.peoplety.record.entity.dto.StreetDTO
     * @author LiFucheng
     * @date: 2022/5/14 11:04
     */
    @HeaderChecker
    @GetMapping("/get/areas")
    public List<Area> getAreas() {
        return areaService.getAreas();
    }

    /**
     * @description: 获取卷宗类型列表
     * @param: pageNum
     * @param: pageSize
     * @return: com.github.pagehelper.PageInfo<gaozhi.online.peoplety.record.entity.RecordType>
     * @author LiFucheng
     * @date: 2022/5/14 11:21
     */
    @HeaderChecker
    @GetMapping("/get/record_type")
    public List<RecordType> getRecordType() {
        return recordTypeService.getRecordTypes();
    }
}
