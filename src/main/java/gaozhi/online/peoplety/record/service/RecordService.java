package gaozhi.online.peoplety.record.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import gaozhi.online.peoplety.entity.*;
import gaozhi.online.peoplety.entity.dto.RecordDTO;
import gaozhi.online.peoplety.record.mapper.CommentMapper;
import gaozhi.online.peoplety.record.mapper.RecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    private final FavoriteService favoriteService;

    @Autowired
    public RecordService(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

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
    public RecordDTO getRecordDTOById(long recordId, long userid) {
        //当前卷宗
        Record record = recordMapper.selectById(recordId);
        return wrapRecordDTO(record, userid);
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
    public PageInfo<Record> getRecordsByArea(int areaId, int pageNum, int pageSize, List<Integer> selectedTypes) {
        PageHelper.startPage(pageNum, pageSize);
        List<Record> records;
        if (selectedTypes == null || selectedTypes.size() == 0) {
            records = recordMapper.selectByAreaId(areaId, true);
        } else {
            records = recordMapper.selectByAreaIdAndType(areaId, true, selectedTypes);
        }
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
    private RecordDTO wrapRecordDTO(Record record, long userid) {
        RecordDTO recordDTO = new RecordDTO();
        if (record == null) {
            return recordDTO;
        }
        recordDTO.setRecord(record);
        //父卷宗
        if (record.getParentId() != 0) {
            Record parent = recordMapper.selectById(record.getParentId());
            recordDTO.setParent(parent);
        }
        //相关卷宗数量
        recordDTO.setChildNum(recordMapper.selectChildCountById(record.getId(), true)+record.getParentId()!=0?1:0);
        //评论数量
        recordDTO.setCommentNum(commentMapper.selectCommentCountByRecordId(record.getId()));
        //收藏数量
        recordDTO.setFavoriteNum(favoriteService.getRecordFavoriteCount(record.getId()));
        //我是否收藏
        recordDTO.setFavorite(favoriteService.getRecordFavorite(userid, record.getId()));
        //收藏条目
        if (recordDTO.getFavorite() != null) {
            recordDTO.setItem(favoriteService.getFavoriteItemByFavoriteInfo(recordDTO.getFavorite().getId(), record.getId()));
        }
        return recordDTO;
    }

    /**
     * @description: 根据id获取卷宗信息
     * @param: id
     * @return: gaozhi.online.peoplety.record.entity.Record
     * @author LiFucheng
     * @date: 2022/6/1 12:32
     */
    public Record getVisibleRecordById(long id) {
        return recordMapper.selectVisibleById(id, true);
    }

    /**
     * @description: 根据id获取评论
     * @param: id
     * @return: gaozhi.online.peoplety.record.entity.Comment
     * @author LiFucheng
     * @date: 2022/6/1 13:27
     */
    public Comment getCommentById(long id) {
        return commentMapper.selectById(id);
    }

    /**
     * @description: 获取用户发布的卷宗的数量
     * @param: userid
     * @return: long
     * @author LiFucheng
     * @date: 2022/6/3 19:00
     */
    public long countRecordNumByUserId(long userid) {
        return recordMapper.countRecordNumByUserId(userid, true);
    }
    /**
     * @description: 获取用户关注的用户的记录
     * @param: userid
	 * @param: pageNum
	 * @param: pageSize
     * @return: com.github.pagehelper.PageInfo<gaozhi.online.peoplety.record.entity.Record>
     * @author LiFucheng
     * @date: 2022/6/14 12:41
     */
    public PageInfo<Record> getAttentionUserRecordsByUserid(long userid, int pageNum, int pageSize) {
        //子卷宗
        PageHelper.startPage(pageNum, pageSize);
        List<Record> records = recordMapper.getAttentionUserRecordsByUserid(userid);
        return new PageInfo<>(records);
    }
}
