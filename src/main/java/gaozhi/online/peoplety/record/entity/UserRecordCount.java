package gaozhi.online.peoplety.record.entity;

import lombok.Data;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 一些通用数据
 * @date 2022/6/3 18:54
 */
@Data
public class UserRecordCount {
    private long userid;
    private long recordNum;
    private long favoriteNum;
}
