package gaozhi.online.peoplety.record.mapper;

import gaozhi.online.peoplety.entity.RecordType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 卷宗类型
 * @date 2022/5/14 13:26
 */
@Mapper
public interface RecordTypeMapper {
    @Select("select id,parent_id parentId,name,description,enable,grade,minimum " +
            "from record_type")
    List<RecordType> getAll();
}
