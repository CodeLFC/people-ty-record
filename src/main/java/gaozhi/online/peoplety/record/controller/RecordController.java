package gaozhi.online.peoplety.record.controller;

import com.github.pagehelper.PageInfo;
import gaozhi.online.base.exception.SQLBusinessException;
import gaozhi.online.base.exception.enums.SQLBusinessExceptionEnum;
import gaozhi.online.base.interceptor.HeaderChecker;
import gaozhi.online.base.result.Result;
import gaozhi.online.peoplety.record.checker.TokenChecker;
import gaozhi.online.peoplety.record.entity.*;
import gaozhi.online.peoplety.record.entity.dto.RecordDTO;
import gaozhi.online.peoplety.record.service.RecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotNull;

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

    /**
     * @description: 发布卷宗
     * @param: record
     * @return: gaozhi.online.peoplety.record.entity.Record
     * @author LiFucheng
     * @date: 2022/5/14 9:59
     */
    @HeaderChecker
    @PostMapping("/post/record")
    public Record publishRecord(@RequestAttribute(TokenChecker.HEADER_CHECKER_NAME) Token token,@RequestAttribute(TokenChecker.HEADER_ATTRIBUTE_IP) String ip, @RequestBody @NotNull Record record) {
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
     * @param: streetId 街道id
     * @return: gaozhi.online.peoplety.record.entity.dto.RecordDTO
     * @author LiFucheng
     * @date: 2022/5/14 9:59
     */
    @HeaderChecker
    @GetMapping("/get/record")
    public RecordDTO getRecordInfo(@NotNull Long recordId, Integer childPage, Integer childPageSize, Integer commentPage, Integer commentPageSize) {
        return recordService.getRecordById(recordId, childPage, childPageSize, commentPage, commentPageSize);
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
    public PageInfo<Record> getUserRecordsByPage(@NotNull Long userid, @NotNull Integer pageNum, @NotNull Integer pageSize) {
        return recordService.getRecordsByUserid(userid, pageNum, pageSize);
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
    public PageInfo<Record> getAreaRecordsByPage(@NotNull Integer areaId, @NotNull Integer pageNum, @NotNull Integer pageSize) {
        return recordService.getRecordsByArea(areaId, pageNum, pageSize);
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
    public Comment commentRecord(@RequestAttribute(TokenChecker.HEADER_CHECKER_NAME) Token token, @RequestAttribute(TokenChecker.HEADER_ATTRIBUTE_IP) String ip,@RequestBody @NotNull Comment comment) {
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
     * @description: TODO 删除评论
     * @author LiFucheng
     * @date 2022/5/24 17:13
     * @version 1.0
     */
    @HeaderChecker
    @DeleteMapping("/delete/comment")
    public Result deleteComment(@NotNull Long id) {
        int res = recordService.deleteComment(id);
        if (res <= 0) {
            //数据库异常创建失败
            throw new SQLBusinessException(SQLBusinessExceptionEnum.DELETE_ERROR, "数据库删除失败");
        }
        return Result.success();
    }
    /**
     * @description: TODO 修改可见性
     * @author LiFucheng
     * @date 2022/5/24 17:13
     * @version 1.0
     */
    @HeaderChecker
    @PatchMapping("/patch/record/visible")
    public Result updateRecordVisible(@NotNull Long id,@NotNull Boolean visible) {
        int res = recordService.updateVisible(id,visible);
        if (res <= 0) {
            //数据库异常创建失败
            throw new SQLBusinessException(SQLBusinessExceptionEnum.UPDATE_ERROR, "数据库删除失败");
        }
        return Result.success();
    }
}
