package gaozhi.online.peoplety.record.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import gaozhi.online.peoplety.record.entity.Comment;
import gaozhi.online.peoplety.record.entity.Record;
import gaozhi.online.peoplety.record.entity.dto.RecordDTO;
import gaozhi.online.peoplety.record.mapper.CommentMapper;
import gaozhi.online.peoplety.record.mapper.RecordMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO   卷宗服务
 * @date 2022/5/14 14:58
 */
@Service
public class RecordService {
    @Resource
    private RecordMapper recordMapper;
    @Resource
    private CommentMapper commentMapper;

    /**
     * @description: 发布卷宗
     * @param: record
     * @return: int
     * @author LiFucheng
     * @date: 2022/5/14 15:53
     */
    public int publish(Record record) {
        return recordMapper.insert(record);
    }

    public int publishComment(Comment comment) {
        return commentMapper.insert(comment);
    }

    /**
     * @description: 删除卷宗 供管理员调用
     * @param: recordId
     * @return: int
     * @author LiFucheng
     * @date: 2022/5/14 15:53
     */
    public int deleteRecord(long recordId) {
        return recordMapper.delete(recordId);
    }

    /**
     * @description: TODO 更改可见性
     * @author LiFucheng
     * @date 2022/5/24 17:09
     * @version 1.0
     */
    public int updateVisible(long recordId, boolean visible) {
        return recordMapper.updateEnable(recordId, visible);
    }

    /**
     * @description: TODO 删除评论
     * @author LiFucheng
     * @date 2022/5/24 17:10
     * @version 1.0
     */
    public int deleteComment(long id) {
        return commentMapper.delete(id);
    }

    /**
     * @description: 获取record实例类型
     * @param: recordId
     * @param: childPage
     * @param: childPageSize
     * @param: commentPage
     * @param: commentPageSize
     * @return: gaozhi.online.peoplety.record.entity.dto.RecordDTO
     * @author LiFucheng
     * @date: 2022/5/14 15:45
     */
    public RecordDTO getRecordDTOById(long recordId) {
        //当前卷宗
        Record record = recordMapper.selectById(recordId, true);
        return wrapRecordDTO(record);
    }

    /**
     * @description: 根据地区获取记录
     * @param: areaId
     * @param: pageNum
     * @param: pageSize
     * @return: com.github.pagehelper.PageInfo<gaozhi.online.peoplety.record.entity.dto.RecordDTO>
     * @author LiFucheng
     * @date: 2022/5/14 15:52
     */
    public PageInfo<Record> getRecordsByArea(int areaId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Record> records = recordMapper.selectByAreaId(areaId, true);
        return new PageInfo<>(records);
    }

    /**
     * @description: 获取用户发布的卷宗
     * @param: userid
     * @param: pageNum
     * @param: pageSize
     * @return: com.github.pagehelper.PageInfo<gaozhi.online.peoplety.record.entity.dto.RecordDTO>
     * @author LiFucheng
     * @date: 2022/5/14 15:56
     */
    public PageInfo<Record> getRecordsByUserid(long userid, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Record> records = recordMapper.selectByUserid(userid, true);
        return new PageInfo<>(records);
    }

    /**
     * @description: 获取卷宗的子卷宗列表
     * @param: recordId
     * @param: pageNum
     * @param: pageSize
     * @return: com.github.pagehelper.PageInfo<gaozhi.online.peoplety.record.entity.Record>
     * @author LiFucheng
     * @date: 2022/5/31 15:37
     */
    public PageInfo<Record> getChildRecord(long recordId, int pageNum, int pageSize) {
        //子卷宗
        PageHelper.startPage(pageNum, pageSize);
        List<Record> records = recordMapper.selectByParentId(recordId, true);
        return new PageInfo<>(records);
    }

    /**
     * @description: 获取卷宗的评论列表
     * @param: recordId
     * @param: pageNum
     * @param: pageSize
     * @return: com.github.pagehelper.PageInfo<gaozhi.online.peoplety.record.entity.Comment>
     * @author LiFucheng
     * @date: 2022/5/31 15:38
     */
    public PageInfo<Comment> getComments(long recordId, int pageNum, int pageSize) {
        //评论
        PageHelper.startPage(pageNum, pageSize);
        List<Comment> comments = commentMapper.selectByRecordId(recordId);
        return new PageInfo<>(comments);
    }

    /**
     * @description: 把record包装成为DTO
     * @param: record
     * @param: childPage
     * @param: childPageSize
     * @param: commentPage
     * @param: commentPageSize
     * @return: gaozhi.online.peoplety.record.entity.dto.RecordDTO
     * @author LiFucheng
     * @date: 2022/5/14 15:44
     */
    private RecordDTO wrapRecordDTO(Record record) {
        RecordDTO recordDTO = new RecordDTO();
        if (record == null) {
            return recordDTO;
        }
        recordDTO.setRecord(record);
        //父卷宗
        if (record.getParentId() != 0) {
            Record parent = recordMapper.selectById(record.getParentId(), true);
            recordDTO.setParent(parent);
        }
        //子卷宗数量
        recordDTO.setChildNum(recordMapper.selectChildCountById(record.getId()));
        //评论数量
        recordDTO.setCommentNum(commentMapper.selectCommentCountByRecordId(record.getId()));
        //收藏数量  ---  test
        recordDTO.setFavorite(true);
        recordDTO.setFavoriteNum(1000000);
        return recordDTO;
    }
}
