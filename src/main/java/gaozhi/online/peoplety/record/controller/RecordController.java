package gaozhi.online.peoplety.record.controller;

import com.github.pagehelper.PageInfo;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import gaozhi.online.base.exception.SQLBusinessException;
import gaozhi.online.base.exception.enums.SQLBusinessExceptionEnum;
import gaozhi.online.base.interceptor.HeaderChecker;
import gaozhi.online.base.result.Result;
import gaozhi.online.peoplety.entity.*;
import gaozhi.online.peoplety.entity.dto.RecordDTO;
import gaozhi.online.peoplety.entity.vo.UserRecordCountVO;
import gaozhi.online.peoplety.exception.UserException;
import gaozhi.online.peoplety.exception.enums.UserExceptionEnum;
import gaozhi.online.peoplety.record.service.FavoriteService;
import gaozhi.online.peoplety.record.service.RecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 卷宗记录
 * @date 2022/5/13 19:11
 */

@Validated
@RestController
@RequestMapping("general/record")
@Slf4j
public class RecordController {

    @Autowired
    private RecordService recordService;
    @Autowired
    private FavoriteService favoriteService;
    private final Gson gson = new Gson();

    /**
     * @description: 发布卷宗
     * @param: record
     * @return: gaozhi.online.peoplety.record.entity.Record
     * @author LiFucheng
     * @date: 2022/5/14 9:59
     */
    @HeaderChecker
    @PostMapping("/post/record")
    public Record publishRecord(@RequestAttribute(HeaderChecker.accessToken) Token token, @RequestAttribute(HeaderChecker.rpcClientIp) String ip, @RequestBody @NotNull Record record) {
        record.setUserid(token.getUserid());
        record.setTime(System.currentTimeMillis());
        record.setIp(ip);
        int res = recordService.publish(record);
        if (res <= 0) {
            //数据库异常创建失败
            throw new SQLBusinessException(SQLBusinessExceptionEnum.INSERT_ERROR, "数据库插入失败");
        }
        return record;
    }

    /**
     * @description: 获取单个卷宗的详情
     * @param: recordId
     * @return: gaozhi.online.peoplety.record.entity.dto.RecordDTO
     * @author LiFucheng
     * @date: 2022/5/14 9:59
     */
    @HeaderChecker
    @GetMapping("/get/record")
    public RecordDTO getRecordInfo(@RequestAttribute(HeaderChecker.accessToken) Token token, @NotNull Long recordId) {
        return recordService.getRecordDTOById(recordId, token.getUserid());
    }

    /**
     * @description: 获取卷宗的子卷宗
     * @param: recordId
     * @param: pageNum
     * @param: pageSize
     * @return: com.github.pagehelper.PageInfo<gaozhi.online.peoplety.record.entity.Record>
     * @author LiFucheng
     * @date: 2022/5/31 15:55
     */
    @HeaderChecker
    @GetMapping("/get/record/child")
    public PageInfo<Record> getRecordChilds(@NotNull Long recordId, @NotNull Integer pageNum, @NotNull Integer pageSize) {
        return recordService.getChildRecord(recordId, pageNum, pageSize);
    }

    /**
     * @description: 获取卷宗的评论
     * @param: recordId
     * @param: pageNum
     * @param: pageSize
     * @return: com.github.pagehelper.PageInfo<gaozhi.online.peoplety.record.entity.Comment>
     * @author LiFucheng
     * @date: 2022/5/31 15:54
     */
    @HeaderChecker
    @GetMapping("/get/record/comment")
    public PageInfo<Comment> getRecordComments(@NotNull Long recordId, @NotNull Integer pageNum, @NotNull Integer pageSize) {
        return recordService.getComments(recordId, pageNum, pageSize);
    }

    /**
     * @description: 根据用户id获取卷宗列表
     * @param: userid 如果不为null则查找特定用户
     * @param: key
     * @param: desc
     * @param: pageNum
     * @param: pageSize
     * @return: com.github.pagehelper.PageInfo<gaozhi.online.peoplety.record.entity.dto.RecordDTO>
     * @author LiFucheng
     * @date: 2022/5/14 10:00
     */
    @HeaderChecker
    @GetMapping("/get/user/records")
    public PageInfo<Record> getUserRecordsByUserid(@NotNull Long userid, @NotNull Integer pageNum, @NotNull Integer pageSize) {
        return recordService.getRecordsByUserid(userid, pageNum, pageSize);
    }

    /**
     * @description: 根据用户id获取用户关注用户发布的内容    ---- 数据库分库后会产生跨库联表查询问题，拟采用表同步方法同步friend表
     * @param: userid
     * @param: pageNum
     * @param: pageSize
     * @return: com.github.pagehelper.PageInfo<gaozhi.online.peoplety.record.entity.Record>
     * @author LiFucheng
     * @date: 2022/6/13 20:33
     */
    @HeaderChecker
    @GetMapping("/get/attention/records")
    public PageInfo<Record> getAttentionUserRecordsByUserid(@RequestAttribute(HeaderChecker.accessToken) Token token, @NotNull Integer pageNum, @NotNull Integer pageSize) {
        return recordService.getAttentionUserRecordsByUserid(token.getUserid(), pageNum, pageSize);
    }

    /**
     * @description: 根据地区获取卷宗列表
     * @param: areaId
     * @param: pageNum
     * @param: pageSize
     * @return: com.github.pagehelper.PageInfo<gaozhi.online.peoplety.record.entity.dto.RecordDTO>
     * @author LiFucheng
     * @date: 2022/5/14 15:58
     */
    @HeaderChecker
    @GetMapping("/get/area/records")
    public PageInfo<Record> getAreaRecordsByPage(@NotNull Integer areaId, @NotNull Integer pageNum, @NotNull Integer pageSize, String selectedTypes) {
        // log.info("types:{}", selectedTypes);
        List<Integer> selectList = gson.fromJson(selectedTypes, new TypeToken<List<Integer>>() {
        }.getType());
        return recordService.getRecordsByArea(areaId, pageNum, pageSize, selectList);
    }

    /**
     * @description: 对卷宗进行点评
     * @param: comment
     * @return: gaozhi.online.peoplety.record.entity.Comment
     * @author LiFucheng
     * @date: 2022/5/14 10:01
     */
    @HeaderChecker
    @PostMapping("/post/comment")
    public Comment commentRecord(@RequestAttribute(HeaderChecker.accessToken) Token token, @RequestAttribute(HeaderChecker.rpcClientIp) String ip, @RequestBody @NotNull Comment comment) {
        comment.setUserid(token.getUserid());
        comment.setTime(System.currentTimeMillis());
        comment.setIp(ip);
        int res = recordService.publishComment(comment);
        if (res <= 0) {
            //数据库异常创建失败
            throw new SQLBusinessException(SQLBusinessExceptionEnum.INSERT_ERROR, "数据库插入失败");
        }
        return comment;
    }

    /**
     * @description: TODO 删除卷宗，实际操作为修改可见性，真实删除接口只有管理员可以访问
     * @author LiFucheng
     * @date 2022/5/24 17:13
     * @version 1.0
     */
    @HeaderChecker
    @DeleteMapping("/delete/record")
    public Result updateRecordVisible(@RequestAttribute(HeaderChecker.accessToken) Token token, @NotNull Long id) {
        Record record = recordService.getVisibleRecordById(id);
        if (record == null) {
            throw new SQLBusinessException(SQLBusinessExceptionEnum.DELETE_ERROR, "卷宗已删除");
        }
        if (token.getUserid() != record.getUserid()) {
            throw new UserException(UserExceptionEnum.USER_AUTH_ERROR, "不能删除其他人的内容");
        }
        int res = recordService.updateVisible(id, false);
        if (res <= 0) {
            //数据库异常修改失败
            throw new SQLBusinessException(SQLBusinessExceptionEnum.UPDATE_ERROR, "删除失败");
        }
        return Result.success();
    }

    /**
     * @description: 删除评论
     * @param: token
     * @param: id
     * @return: gaozhi.online.base.result.Result
     * @author LiFucheng
     * @date: 2022/6/1 12:35
     */
    @HeaderChecker
    @DeleteMapping("/delete/comment")
    public Result deleteComment(@RequestAttribute(HeaderChecker.accessToken) Token token, @NotNull Long id) {
        Comment comment = recordService.getCommentById(id);
        if (comment == null) {
            throw new SQLBusinessException(SQLBusinessExceptionEnum.DELETE_ERROR, "评论已删除");
        }
        if (token.getUserid() != comment.getUserid()) {
            throw new UserException(UserExceptionEnum.USER_AUTH_ERROR, "不能删除其他人的内容");
        }
        int res = recordService.deleteComment(id);
        if (res <= 0) {
            //数据库异常修改失败
            throw new SQLBusinessException(SQLBusinessExceptionEnum.UPDATE_ERROR, "删除失败");
        }
        return Result.success();
    }

    /**
     * @description: 获取一些统计数据
     * @param: token
     * @param: userid
     * @return: gaozhi.online.peoplety.record.entity.RecordCount
     * @author LiFucheng
     * @date: 2022/6/3 18:58
     */
    @HeaderChecker
    @GetMapping("/get/count")
    public UserRecordCountVO getRecordCount(@RequestAttribute(HeaderChecker.accessToken) Token token, @NotNull Long userid) {
        UserRecordCountVO userRecordCount = new UserRecordCountVO();
        userRecordCount.setUserid(userid);
        long recordNum = recordService.countRecordNumByUserId(userid);
        userRecordCount.setRecordNum(recordNum);
        userRecordCount.setFavoriteNum(favoriteService.getFavoriteCountByUserid(userid, token.getUserid() == userid));
        return userRecordCount;
    }
}
