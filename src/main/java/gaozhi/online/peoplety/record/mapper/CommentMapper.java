package gaozhi.online.peoplety.record.mapper;

import gaozhi.online.peoplety.record.entity.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {
    @Insert("insert into comment (userid,record_id,content,url,time,ip) values(#{userid},#{recordId},#{content},#{url},#{time},#{ip})")
    @SelectKey(keyProperty = "id", keyColumn = "id", before = false, resultType = Long.class, statement = "select last_insert_id()")
    int insert(Comment comment);

    @Select("select id,userid,record_id recordId,content,url,time,ip" +
            " from comment " +
            "where record_id = #{recordId}")
    List<Comment> selectByRecordId(long recordId);

    @Delete("delete from comment " +
            "where id = #{id}")
    int delete(long id);

}
