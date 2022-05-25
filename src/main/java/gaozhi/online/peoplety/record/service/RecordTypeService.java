package gaozhi.online.peoplety.record.service;

import gaozhi.online.peoplety.record.entity.RecordType;
import gaozhi.online.peoplety.record.mapper.RecordTypeMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 卷宗类型
 * @date 2022/5/14 13:25
 */
@Service
public class RecordTypeService {
    @Resource
    private RecordTypeMapper recordTypeMapper;

    public List<RecordType> getRecordTypes() {
        return recordTypeMapper.getAll();
    }
}
