package cn.com.startai.qxsdk.busi.common;

import java.util.Arrays;

import cn.com.startai.qxsdk.db.bean.DeviceBean;

/**
 * Created by Robin on 2019/3/18.
 * 419109715@qq.com 彬影
 */
public class PassthroughReq {

    private DeviceBean deviceBean;
    private byte[] dataArr;
    private String dataStr;

    @Override
    public String toString() {
        return "PassthroughReq{" +
                "deviceBean=" + deviceBean +
                ", dataArr=" + Arrays.toString(dataArr) +
                '}';
    }

    public DeviceBean getDeviceBean() {
        return deviceBean;
    }

    public void setDeviceBean(DeviceBean deviceBean) {
        this.deviceBean = deviceBean;
    }

    public String getDataStr() {
        return dataStr;
    }

    public void setDataStr(String dataStr) {
        this.dataStr = dataStr;
    }

    public byte[] getDataArr() {
        return dataArr;
    }

    public void setDataArr(byte[] dataArr) {
        this.dataArr = dataArr;
    }

    public PassthroughReq(DeviceBean deviceBean, byte[] dataArr) {
        this.deviceBean = deviceBean;
        this.dataArr = dataArr;
    }

    public PassthroughReq(DeviceBean deviceBean, String dataStr) {
        this.deviceBean = deviceBean;
        this.dataStr = dataStr;
    }
}
