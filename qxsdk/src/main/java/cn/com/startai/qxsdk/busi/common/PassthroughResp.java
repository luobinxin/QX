package cn.com.startai.qxsdk.busi.common;

import java.util.Arrays;

import cn.com.startai.qxsdk.db.bean.DeviceBean;

/**
 * Created by Robin on 2019/3/18.
 * 419109715@qq.com 彬影
 */
public class PassthroughResp extends BaseResp {


    private DeviceBean deviceBean;
    private String ip;
    private int port;
    private byte[] dataArr;
    private String dataStr;

    @Override
    public String toString() {
        return "PassthroughResp{" +
                "deviceBean=" + deviceBean +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", dataArr=" + Arrays.toString(dataArr) +
                ", dataStr='" + dataStr + '\'' +
                ", errcode='" + errcode + '\'' +
                ", errmsg='" + errmsg + '\'' +
                '}';
    }

    public String getDataStr() {
        return dataStr;
    }

    public void setDataStr(String dataStr) {
        this.dataStr = dataStr;
    }

    public PassthroughResp() {
    }

    public PassthroughResp(int result, DeviceBean deviceBean, String ip, int port, byte[] dataArr, String dataStr) {
        super(result);
        this.deviceBean = deviceBean;
        this.ip = ip;
        this.port = port;
        this.dataArr = dataArr;
        this.dataStr = dataStr;
    }

    public PassthroughResp(int result, String ip, int port, byte[] dataArr, String dataStr) {
        super(result);
        this.ip = ip;
        this.port = port;
        this.dataArr = dataArr;
        this.dataStr = dataStr;
    }

    public PassthroughResp(int result, DeviceBean deviceBean, byte[] dataArr, String dataStr) {
        super(result);
        this.deviceBean = deviceBean;
        this.dataArr = dataArr;
        this.dataStr = dataStr;
    }


    public DeviceBean getDeviceBean() {
        return deviceBean;
    }

    public void setDeviceBean(DeviceBean deviceBean) {
        this.deviceBean = deviceBean;
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


    public byte[] getDataArr() {
        return dataArr;
    }

    public void setDataArr(byte[] dataArr) {
        this.dataArr = dataArr;
    }

}
