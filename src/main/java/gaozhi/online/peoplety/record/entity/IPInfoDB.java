package gaozhi.online.peoplety.record.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.LinkedList;
import java.util.List;

import lombok.Data;

@Data
public class IPInfoDB {
    private String ip;
    private String data;
    private String msg;
    private long time;

    public IPInfoDB setIPInfo(IPInfo ipInfo) {
        if (ipInfo == null) return this;
        ip = ipInfo.getIp();
        data = new Gson().toJson(ipInfo.getData());
        msg = ipInfo.getMsg();
        return this;
    }

    public IPInfo getIPInfo() {
        IPInfo ipInfo = new IPInfo();
        ipInfo.setIp(ip);
        ipInfo.setMsg(msg);
        ipInfo.setData(new Gson().fromJson(data, new TypeToken<List<String>>() {
        }.getType()));
        if (ipInfo.getData() == null) {
            ipInfo.setData(new LinkedList<>());
        }
        return ipInfo;
    }
}