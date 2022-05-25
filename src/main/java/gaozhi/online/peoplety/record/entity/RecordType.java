package gaozhi.online.peoplety.record.entity;

import lombok.Data;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 记录类型
 * @date 2022/5/14 11:05
 */
@Data
public class RecordType {
    private int id;
    private int parentId;
    private String name;
    private String description;
    private boolean enable;
    private int grade;
    private boolean minimum;
}
