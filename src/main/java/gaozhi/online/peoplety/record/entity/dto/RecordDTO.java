package gaozhi.online.peoplety.record.entity.dto;

import gaozhi.online.peoplety.record.entity.Favorite;
import gaozhi.online.peoplety.record.entity.Record;
import lombok.Data;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 父子卷宗
 * @date 2022/5/14 9:39
 */
@Data
public class RecordDTO {
    private Record record;
    private Record parent;
    //收藏数量
    private long favoriteNum;
    //子数量
    private long childNum;
    //评论数量
    private long commentNum;
    //所收藏的收藏夹
    private Favorite favorite;
    //收藏的条目
    private Favorite.Item item;
}
