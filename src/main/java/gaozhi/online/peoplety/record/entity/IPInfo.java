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
     *     "data": [
     *      *         "中国",            // 国家（极少为空）
     *      *         "四川",          // 省份/自治区/直辖市（少数为空）
     *      *         "成都",          // 地级市（部份为空）
     *      *         "锦江区",          // 区/县（部份为空）
     *      *         "电信",            // 运营商
     *      *         "610000",          // 邮政编码
     *      *         "028"              // 地区区号
     *      *     ]
     */
    private List<String> data;
    private String msg;
}
