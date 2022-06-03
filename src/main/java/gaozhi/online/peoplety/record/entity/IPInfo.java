package gaozhi.online.peoplety.record.entity;

import lombok.Data;

import java.util.List;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO IP地址信息
 * @date 2022/5/30 14:59
 */
@Data
public class IPInfo {
    private String ret;
    private String ip;
    /**
     * 国家（极少为空）   // 省份/自治区/直辖市（少数为空） // 地级市（部份为空） // 区/县（部份为空）    // 运营商 // 邮政编码  // 地区区号
     */
    private List<String> data;
    private String msg;
}
