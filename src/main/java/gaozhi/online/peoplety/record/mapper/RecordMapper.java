package gaozhi.online.peoplety.record.mapper;

import gaozhi.online.peoplety.entity.Record;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 记录
 * @date 2022/5/14 13:46
 */
@Mapper
public interface RecordMapper {
    @Insert("insert into record(parent_id,userid,area_id,record_type_id,title,description,content,imgs,url,time,ip,top)" +
            " values(#{parentId},#{userid},#{areaId},#{recordTypeId},#{title},#{description},#{content},#{imgs},#{url},#{time},#{ip},#{top})")
    @SelectKey(keyProperty = "id", keyColumn = "id", before = false, resultType = Long.class, statement = "select last_insert_id()")
    int insert(Record record);

    /**
     * @description: 设置可见与不可见
     * @param: id
     * @param: enable
     * @return: int
     * @author LiFucheng
     * @date: 2022/5/14 14:19
     */
    @Update("update record " +
            "set enable = #{enable} " +
            "where id = #{id}")
    int updateEnable(long id, boolean enable);

    /**
     * @description: 用户设置置顶卷宗
     * @param: id
     * @param: top
     * @return: int
     * @author LiFucheng
     * @date: 2022/5/14 14:34
     */
    @Update("update record " +
            "set top = #{top} " +
            "where id = #{id}")
    int updateTop(long id, boolean top);

    /**
     * @description: 删除记录
     * @param: id
     * @return: int
     * @author LiFucheng
     * @date: 2022/5/14 14:20
     */
    @Delete("delete from record " +
            "where id = #{id}")
    int delete(long id);

    @Select("select " +
            "id,parent_id parentId,userid,area_id areaId,record_type_id recordTypeId,enable,title,description,content,imgs,url,time,ip,top " +
            "from record " +
            "where id = #{id} ")
    Record selectById(long id);

    @Select("select " +
            "id,parent_id parentId,userid,area_id areaId,record_type_id recordTypeId,enable,title,description,content,imgs,url,time,ip,top " +
            "from record " +
            "where id = #{id} and enable = #{enable}")
    Record selectVisibleById(long id, boolean enable);

    @Select("select " +
            "id,parent_id parentId,userid,area_id areaId,record_type_id recordTypeId,enable,title,description,content,imgs,url,time,ip,top " +
            "from record " +
            "where userid = #{userid}  and enable = #{enable} " +
            "order by top desc, id desc")
    List<Record> selectByUserid(long userid, boolean enable);

    @Select("select " +
            "id,parent_id parentId,userid,area_id areaId,record_type_id recordTypeId,enable,title,description,content,imgs,url,time,ip,top " +
            "from record " +
            "where parent_id = #{parentId}  and enable = #{enable} " +
            "order by id desc")
    List<Record> selectByParentId(long parentId, boolean enable);

    @Select("select " +
            "id,parent_id parentId,userid,area_id areaId,record_type_id recordTypeId,enable,title,description,content,imgs,url,time,ip,top " +
            "from record " +
            "where area_id  = #{areaId}  and enable = #{enable} " +
            "order by id desc")
    List<Record> selectByAreaId(int areaId, boolean enable);

    @Select({"<script>",
            "select " +
                    "id,parent_id parentId,userid,area_id areaId,record_type_id recordTypeId,enable,title,description,content,imgs,url,time,ip,top " +
                    "from record " +
                    "where area_id  = #{areaId}  and enable = #{enable} and record_type_id in " +
                    "<foreach collection='selectedTypes' item='item' index='index' open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "order by id desc",
            "</script>"})
    List<Record> selectByAreaIdAndType(int areaId, boolean enable, List<Integer> selectedTypes);

    @Select("select " +
            "id,parent_id parentId,userid,area_id areaId,record_type_id recordTypeId,enable,title,description,content,imgs,url,time,ip,top " +
            "from record " +
            "where area_id  = #{areaId} and enable = #{enable} and time between #{lowTime} and #{highTime} " +
            "order by id desc")
    List<Record> selectBetweenTime(int areaId, long lowTime, long highTime, boolean enable);

    @Select("select " +
            "count(id) " +
            "from record " +
            "where parent_id = #{id} and enable = #{enable} ")
    long selectChildCountById(long id, boolean enable);

    @Select("select count(id) " +
            "from record " +
            "where userid = #{userid} and  enable = #{enable} ")
    long countRecordNumByUserId(long userid, boolean enable);
    /** 
     * @description: 联表查询，如果分库会产生问题 
     * @param: userid 
     * @return: java.util.List<gaozhi.online.peoplety.record.entity.Record> 
     * @author LiFucheng
     * @date: 2022/6/14 12:55
     */ 
    @Select("select " +
            "record.id,parent_id parentId,record.userid,area_id areaId,record_type_id recordTypeId,enable,title,description,content,imgs,url,record.time,ip,top " +
            "from record left join friend on friend.friend_id = record.userid " +
            "where (record.userid=#{userid} or friend.userid  = #{userid}) and record.enable = true " +
            "order by record.id desc")
    List<Record> getAttentionUserRecordsByUserid(long userid);
}
