package gaozhi.online.peoplety.record.mapper;

import gaozhi.online.peoplety.entity.IPInfoDB;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 地址信息
 * @date 2022/6/3 19:22
 */
@Mapper
public interface IPInfoMapper {
    @Insert("insert into ip_info(ip,data,time) values(#{ip},#{data},#{time})")
    int insert(IPInfoDB info);

    @Select("select ip,data,time from ip_info " +
            "where ip = #{ip}")
    IPInfoDB selectByIP(String ip);

    @Update("update ip_info " +
            "set data = #{data}, time = #{time} " +
            "where ip = #{ip}")
    int updateIPInfo(IPInfoDB info);
}
