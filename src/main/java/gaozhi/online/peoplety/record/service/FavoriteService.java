package gaozhi.online.peoplety.record.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import gaozhi.online.peoplety.record.entity.Favorite;
import gaozhi.online.peoplety.record.mapper.FavoriteItemMapper;
import gaozhi.online.peoplety.record.mapper.FavoriteMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 收藏夹
 * @date 2022/6/8 15:07
 */
@Service
public class FavoriteService {
    @Resource
    private FavoriteMapper favoriteMapper;
    @Resource
    private FavoriteItemMapper favoriteItemMapper;

    /**
     * @description: 新建收藏夹
     * @param: favorite
     * @return: int
     * @author LiFucheng
     * @date: 2022/6/8 15:37
     */

    public int createFavorite(Favorite favorite) {
        return favoriteMapper.insert(favorite);
    }

    /**
     * @description: 根据ID删除收藏夹
     * @param: favoriteId
     * @return: int
     * @author LiFucheng
     * @date: 2022/6/8 15:37
     */
    public int deleteFavorite(long favoriteId) {
        return favoriteMapper.deleteById(favoriteId);
    }

    /**
     * @description: 修改收藏夹
     * @param: favorite
     * @return: int
     * @author LiFucheng
     * @date: 2022/6/8 15:37
     */
    public int updateFavorite(Favorite favorite) {
        return favoriteMapper.update(favorite);
    }

    /**
     * @description: 根据userid获取收藏夹列表
     * @param: userid
     * @return: java.util.List<gaozhi.online.peoplety.record.entity.Favorite>
     * @author LiFucheng
     * @date: 2022/6/8 15:38
     */
    public PageInfo<Favorite> getFavoriteByUserid(long userid, boolean visible, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Favorite> favorites;
        if (visible) {
            favorites = favoriteMapper.getFavoritesByUserId(userid);
        } else {
            favorites = favoriteMapper.getVisibleFavoritesByUserId(userid);
        }
        return new PageInfo<>(favorites);
    }

    /**
     * @description: 获取收藏夹
     * @param: id
     * @return: gaozhi.online.peoplety.record.entity.Favorite
     * @author LiFucheng
     * @date: 2022/6/8 20:53
     */
    public Favorite getFavoriteById(long id) {
        return favoriteMapper.getFavoriteById(id);
    }

    /**
     * @description: 添加新的收藏条目
     * @param: item
     * @return: int
     * @author LiFucheng
     * @date: 2022/6/8 15:39
     */
    public int addFavoriteItem(Favorite.Item item) {
        return favoriteItemMapper.insert(item);
    }

    /**
     * @description: 删除收藏条目
     * @param: id
     * @return: int
     * @author LiFucheng
     * @date: 2022/6/8 15:39
     */
    public int deleteFavoriteItemById(long id) {
        return favoriteItemMapper.deleteById(id);
    }

    /**
     * @description: 获取收藏夹中的收藏条目
     * @param: favoriteId
     * @return: java.util.List<gaozhi.online.peoplety.record.entity.Favorite.Item>
     * @author LiFucheng
     * @date: 2022/6/8 15:39
     */
    public PageInfo<Favorite.Item> getFavoriteItemsByFavoriteId(long favoriteId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(favoriteItemMapper.getFavoriteItemByFavoriteId(favoriteId));
    }

    /**
     * @description: 查询卷宗是否存在于收藏夹中
     * @param: favoriteId
     * @param: recordId
     * @return: boolean
     * @author LiFucheng
     * @date: 2022/6/8 15:40
     */
    public Favorite.Item getRecordExistedFavorite(long favoriteId, long recordId) {
        return favoriteItemMapper.getFavoriteItemByIds(favoriteId, recordId);
    }

    /**
     * @description: 根据收藏条目id查找条目详情
     * @param: id
     * @return: gaozhi.online.peoplety.record.entity.Favorite.Item
     * @author LiFucheng
     * @date: 2022/6/8 19:44
     */
    public Favorite.Item getFavoriteItemById(long id) {
        return favoriteItemMapper.getFavoriteItemById(id);
    }

    /**
     * @description: 根据卷宗id获取卷宗的收藏数量
     * @param: recordId
     * @return: long
     * @author LiFucheng
     * @date: 2022/6/8 20:42
     */
    public long getRecordFavoriteCount(long recordId) {
        return favoriteItemMapper.getRecordFavoriteCount(recordId);
    }

    /**
     * @description: 用户是否收藏了此卷宗
     * @param: userid
     * @param: recordId
     * @return: boolean
     * @author LiFucheng
     * @date: 2022/6/8 20:57
     */
    public Favorite getRecordFavorite(long userid, long recordId) {
        return favoriteMapper.getRecordFavorite(userid, recordId);
    }

    /**
     * @description: 获取收藏夹的数量
     * @param: userid
     * @return: long
     * @author LiFucheng
     * @date: 2022/6/8 21:47
     */
    public long getFavoriteCountByUserid(long userid) {
        return favoriteMapper.getFavoriteCountByUserId(userid);
    }

    /**
     * @description: 根据收藏信息获取收藏条目的信息
     * @param: favoriteId
     * @param: recordId
     * @return: gaozhi.online.peoplety.record.entity.Favorite.Item
     * @author LiFucheng
     * @date: 2022/6/10 19:24
     */
    public Favorite.Item getFavoriteItemByFavoriteInfo(long favoriteId, long recordId) {
        return favoriteItemMapper.getFavoriteItemByIds(favoriteId, recordId);
    }
}
