package gaozhi.online.peoplety.record.controller;

import com.github.pagehelper.PageInfo;
import gaozhi.online.base.exception.SQLBusinessException;
import gaozhi.online.base.exception.enums.SQLBusinessExceptionEnum;
import gaozhi.online.base.interceptor.HeaderChecker;
import gaozhi.online.base.result.Result;
import gaozhi.online.peoplety.record.checker.TokenChecker;
import gaozhi.online.peoplety.record.entity.Favorite;
import gaozhi.online.peoplety.record.entity.Record;
import gaozhi.online.peoplety.record.entity.Token;
import gaozhi.online.peoplety.record.exception.UserException;
import gaozhi.online.peoplety.record.exception.enums.UserExceptionEnum;
import gaozhi.online.peoplety.record.service.FavoriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 收藏夹控制器
 * @date 2022/6/8 15:40
 */
@Validated
@RestController
@RequestMapping("general/favorite")
@Slf4j
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    /**
     * @description: 根据用户id获取用户创建的收藏夹
     * @param: userid
     * @param: pageNum
     * @param: pageSize
     * @return: com.github.pagehelper.PageInfo<gaozhi.online.peoplety.record.entity.Favorite>
     * @author LiFucheng
     * @date: 2022/6/8 16:44
     */
    @HeaderChecker
    @GetMapping("/get/user/favorites")
    public PageInfo<Favorite> getUserFavorites(@RequestAttribute(TokenChecker.HEADER_CHECKER_NAME) Token token, @NotNull Long userid, @NotNull Integer pageNum, @NotNull Integer pageSize) {
        return favoriteService.getFavoriteByUserid(userid, token.getUserid() == userid, pageNum, pageSize);
    }

    /**
     * @description: 创建收藏夹
     * @param: token
     * @param: favorite
     * @return: gaozhi.online.peoplety.record.entity.Favorite
     * @author LiFucheng
     * @date: 2022/6/8 17:42
     */
    @HeaderChecker
    @PostMapping("/post/favorite")
    public Favorite createFavorite(@RequestAttribute(TokenChecker.HEADER_CHECKER_NAME) Token token, @RequestBody Favorite favorite) {
        favorite.setUserid(token.getUserid());
        favorite.setTime(System.currentTimeMillis());
        int res = favoriteService.createFavorite(favorite);
        if (res <= 0) {
            throw new SQLBusinessException(SQLBusinessExceptionEnum.INSERT_ERROR, "创建收藏夹失败");
        }
        return favorite;
    }

    /**
     * @description: 删除收藏夹
     * @param: token
     * @param: id
     * @return: gaozhi.online.base.result.Result
     * @author LiFucheng
     * @date: 2022/6/8 18:26
     */
    @HeaderChecker
    @DeleteMapping("/delete/favorite")
    public Result deleteFavorite(@RequestAttribute(TokenChecker.HEADER_CHECKER_NAME) Token token, @NotNull Long id) {
        Favorite favorite = favoriteService.getFavoriteById(id);
        if (favorite == null) {
            throw new SQLBusinessException(SQLBusinessExceptionEnum.SELECT_ERROR, "收藏夹不存在");
        }
        if (favorite.getUserid() != token.getUserid()) {
            throw new UserException(UserExceptionEnum.USER_AUTH_ERROR, "不能删除他人的收藏夹");
        }
        //删除收藏夹
        int res = favoriteService.deleteFavorite(id);
        if (res <= 0) {
            throw new SQLBusinessException(SQLBusinessExceptionEnum.DELETE_ERROR, "删除收藏夹失败");
        }
        return Result.success();
    }

    /**
     * @description: 修改收藏夹
     * @param: token
     * @param: favorite
     * @return: gaozhi.online.peoplety.record.entity.Favorite
     * @author LiFucheng
     * @date: 2022/6/8 19:20
     */
    @HeaderChecker
    @PutMapping("/put/favorite")
    public Favorite updateFavorite(@RequestAttribute(TokenChecker.HEADER_CHECKER_NAME) Token token, @RequestBody Favorite favorite) {
        Favorite favoriteDB = favoriteService.getFavoriteById(favorite.getId());
        if (favoriteDB == null) {
            throw new SQLBusinessException(SQLBusinessExceptionEnum.SELECT_ERROR, "收藏夹不存在");
        }
        if (token.getUserid() != favoriteDB.getUserid()) {
            throw new UserException(UserExceptionEnum.USER_AUTH_ERROR, "不能修改他人的收藏夹");
        }
        favorite.setUserid(token.getUserid());
        favorite.setTime(favoriteDB.getTime());
        int res = favoriteService.updateFavorite(favorite);
        if (res <= 0) {
            throw new SQLBusinessException(SQLBusinessExceptionEnum.UPDATE_ERROR, "修改收藏夹失败");
        }
        return favorite;
    }

    /**
     * @description: 收藏卷宗到收藏夹中，不检查卷宗ID
     * @param: token
     * @param: item
     * @return: gaozhi.online.peoplety.record.entity.Favorite.Item
     * @author LiFucheng
     * @date: 2022/6/8 19:29
     */
    @HeaderChecker
    @PostMapping("/post/favorite/item")
    public Favorite.Item favoriteRecord(@RequestAttribute(TokenChecker.HEADER_CHECKER_NAME) Token token, @RequestBody Favorite.Item item) {
        Favorite favorite = favoriteService.getFavoriteById(item.getFavoriteId());
        if (favorite == null || favorite.getUserid() != token.getUserid()) {
            throw new SQLBusinessException(SQLBusinessExceptionEnum.SELECT_ERROR, "收藏夹不存在");
        }
        item.setTime(System.currentTimeMillis());
        Favorite.Item itemDB = favoriteService.getRecordExistedFavorite(item.getFavoriteId(), item.getRecordId());
        if (itemDB != null) {
            throw new SQLBusinessException(SQLBusinessExceptionEnum.SELECT_ERROR, "该内容已收藏");
        }
        int res = favoriteService.addFavoriteItem(item);
        if (res <= 0) {
            throw new SQLBusinessException(SQLBusinessExceptionEnum.INSERT_ERROR, "收藏失败");
        }
        return item;
    }

    /**
     * @description: 删除收藏内容
     * @param: token
     * @param: id
     * @return: gaozhi.online.base.result.Result
     * @author LiFucheng
     * @date: 2022/6/8 19:48
     */
    @HeaderChecker
    @DeleteMapping("/delete/favorite/item")
    public Result deleteFavoriteItem(@RequestAttribute(TokenChecker.HEADER_CHECKER_NAME) Token token, @NotNull Long id) {
        Favorite.Item item = favoriteService.getFavoriteItemById(id);
        if (item == null) {
            throw new SQLBusinessException(SQLBusinessExceptionEnum.SELECT_ERROR, "收藏条目不存在");
        }
        Favorite favorite = favoriteService.getFavoriteById(item.getFavoriteId());
        if (favorite == null || favorite.getUserid() != token.getUserid()) {
            throw new SQLBusinessException(SQLBusinessExceptionEnum.SELECT_ERROR, "收藏夹不存在");
        }
        int res = favoriteService.deleteFavoriteItemById(id);
        if (res <= 0) {
            throw new SQLBusinessException(SQLBusinessExceptionEnum.INSERT_ERROR, "取消收藏失败");
        }
        return Result.success();
    }

    /**
     * @description: 获取收藏夹中的内容
     * @param: favoriteId
     * @param: pageNum
     * @param: pageSize
     * @return: com.github.pagehelper.PageInfo<gaozhi.online.peoplety.record.entity.Favorite.Item>
     * @author LiFucheng
     * @date: 2022/6/8 19:55
     */
    @HeaderChecker
    @GetMapping("/get/favorite/items")
    public PageInfo<Favorite.Item> getFavoriteItemByFavoriteId(@NotNull Long favoriteId, @NotNull Integer pageNum, @NotNull Integer pageSize) {
        return favoriteService.getFavoriteItemsByFavoriteId(favoriteId, pageNum, pageSize);
    }
}
