package gaozhi.online.peoplety.record.entity;

import lombok.Data;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 收藏夹
 * @date 2022/5/14 11:21
 */
@Data
public class Favorite {
    private long id;
    private long userid;
    private String name;
    private String description;
    private long time;

    /**
     * @author LiFucheng
     * @version 1.0
     * @description: TODO 收藏卷宗
     * @date 2022/5/14 11:21
     */
    @Data
    public static class Item {
        private long id;
        private long favoriteId;
        private long recordId;
        private long time;
    }
}
