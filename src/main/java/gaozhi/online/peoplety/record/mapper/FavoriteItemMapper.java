package gaozhi.online.peoplety.record.mapper;

import gaozhi.online.peoplety.record.entity.Favorite;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 收藏表
 * @date 2022/6/8 14:38
 */
@Mapper
public interface FavoriteItemMapper {
    /**
     * @description: 添加收藏条目
     * @param: item
     * @return: int
     * @author LiFucheng
     * @date: 2022/6/8 14:58
     */
    @Insert("insert into favorite_item(favorite_id,record_id,time) values(#{favoriteId},#{recordId},#{time})")
    @SelectKey(keyProperty = "id", keyColumn = "id", before = false, resultType = Long.class, statement = "select last_insert_id()")
    int insert(Favorite.Item item);

    /**
     * @description: 删除收藏条目
     * @param: id
     * @return: int
     * @author LiFucheng
     * @date: 2022/6/8 14:59
     */
    @Delete("delete from favorite_item where id = #{id}")
    int deleteById(long id);

    /**
     * @description: 根据id获取item
     * @param: id
     * @return: gaozhi.online.peoplety.record.entity.Favorite.Item
     * @author LiFucheng
     * @date: 2022/6/8 18:20
     */
    @Select("select id,favorite_id favoriteId,record_id recordId,time " +
            "from favorite_item " +
            "where id = #{id}"
    )
    Favorite.Item getFavoriteItemById(long id);

    /**
     * @description: 查询卷宗是否属于收藏夹 favoriteId
     * @param: favoriteId
     * @param: recordId
     * @return: gaozhi.online.peoplety.record.entity.Favorite.Item
     * @author LiFucheng
     * @date: 2022/6/8 15:32
     */
    @Select("select id,favorite_id favoriteId,record_id recordId,time " +
            "from favorite_item " +
            "where favorite_id = #{favoriteId} and record_id = #{recordId} "
    )
    Favorite.Item getFavoriteItemByIds(long favoriteId, long recordId);

    /**
     * @description: 根据收藏夹获取收藏内容
     * @param: favoriteId
     * @return: java.util.List<gaozhi.online.peoplety.record.entity.Favorite.Item>
     * @author LiFucheng
     * @date: 2022/6/8 15:06
     */
    @Select("select id,favorite_id favoriteId,record_id recordId,time " +
            "from favorite_item " +
            "where favorite_id = #{favoriteId} " +
            "order by id desc")
    List<Favorite.Item> getFavoriteItemByFavoriteId(long favoriteId);

    /**
     * @description: 获得卷宗的收藏数量
     * @param: recordId
     * @return: long
     * @author LiFucheng
     * @date: 2022/6/8 20:41
     */
    @Select("select count(id) " +
            "from favorite_item " +
            "where record_id = #{recordId}")
    long getRecordFavoriteCount(long recordId);
}
