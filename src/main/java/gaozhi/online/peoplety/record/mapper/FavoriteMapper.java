package gaozhi.online.peoplety.record.mapper;

import gaozhi.online.peoplety.record.entity.Favorite;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 收藏夹
 * @date 2022/6/8 14:38
 */
@Mapper
public interface FavoriteMapper {
    /**
     * @description: 添加新的收藏夹
     * @param: favorite
     * @return: int
     * @author LiFucheng
     * @date: 2022/6/8 14:45
     */
    @Insert("insert into favorite(userid,name,description,time,visible) values(#{userid},#{name},#{description},#{time},#{visible})")
    @SelectKey(keyProperty = "id", keyColumn = "id", before = false, resultType = Long.class, statement = "select last_insert_id()")
    int insert(Favorite favorite);

    /**
     * @description: 删除收藏夹
     * @param: id
     * @return: int
     * @author LiFucheng
     * @date: 2022/6/8 14:46
     */
    @Delete("delete from favorite where id = #{id}")
    int deleteById(long id);

    /**
     * @description: 修改收藏夹信息
     * @param: favorite
     * @return: int
     * @author LiFucheng
     * @date: 2022/6/8 15:20
     */
    @Insert("update favorite set name = #{name},description = #{description},visible=#{visible} where id = #{id}")
    int update(Favorite favorite);

    /**
     * @description: 根据用户id选择收藏夹
     * @param: userid
     * @return: java.util.List<gaozhi.online.peoplety.record.entity.Favorite>
     * @author LiFucheng
     * @date: 2022/6/8 14:57
     */
    @Select("select id,userid,name,description,time,visible " +
            "from favorite " +
            "where userid = #{userid}")
    List<Favorite> getFavoritesByUserId(long userid);

    /**
     * @description: 获取可见的收藏夹
     * @param: userid
     * @return: java.util.List<gaozhi.online.peoplety.record.entity.Favorite>
     * @author LiFucheng
     * @date: 2022/6/9 15:01
     */
    @Select("select id,userid,name,description,time,visible " +
            "from favorite " +
            "where userid = #{userid} and visible = true")
    List<Favorite> getVisibleFavoritesByUserId(long userid);

    @Select("select id,userid,name,description,time,visible " +
            "from favorite " +
            "where id = #{id}")
    Favorite getFavoriteById(long id);

    /**
     * @description: select * from 表1 left join 表2   on连接条件   [where条件查询]；
     * @param: userid
     * @param: recordId
     * @return: gaozhi.online.peoplety.record.entity.Favorite
     * @author LiFucheng
     * @date: 2022/6/8 21:22
     */
    @Select("select favorite.id,userid,name,description,favorite.time,visible " +
            "from favorite left join favorite_item on favorite.id=favorite_item.favorite_id " +
            "where userid = #{userid} and record_id = #{recordId}")
    Favorite getRecordFavorite(long userid, long recordId);

    /**
     * @description: 统计收藏夹的数量
     * @param: userid
     * @return: long
     * @author LiFucheng
     * @date: 2022/6/8 21:46
     */
    @Select("select count(id) " +
            "from favorite " +
            "where userid = #{userid}")
    long getFavoriteCountByUserId(long userid);

    @Select("select count(id) " +
            "from favorite " +
            "where userid = #{userid} and visible = true")
    long getVisibleFavoriteCountByUserId(long userid);
}
