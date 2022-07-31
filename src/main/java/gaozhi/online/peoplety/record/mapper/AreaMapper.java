package gaozhi.online.peoplety.record.mapper;

import gaozhi.online.peoplety.entity.Area;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 地区
 * @date 2022/5/14 13:16
 */
@Mapper
public interface AreaMapper {
    @Select("select id,parent_id parentId,post_code postCode,name,description,url,minimum " +
            "from area")
    List<Area> getAll();
}
