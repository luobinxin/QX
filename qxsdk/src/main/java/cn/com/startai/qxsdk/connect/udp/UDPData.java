package cn.com.startai.qxsdk.connect.udp;

import cn.com.startai.qxsdk.connect.BaseData;

/**
 * Created by Robin on 2019/3/20.
 * 419109715@qq.com 彬影
 */
public class UDPData extends BaseData {


    private String ip;
    private int port;

    public UDPData(byte[] data, int port) {
        super(data);
        this.port = port;
    }

    public UDPData(byte[] data) {
        super(data);
    }

    public UDPData(byte[] data, String ip, int port) {
        super(data);
        this.ip = ip;
        this.port = port;
    }

    @Override
    public String toString() {
        return "UDPData{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
