package cn.com.startai.qxsdk.busi.socket.entity;

import cn.com.startai.qxsdk.db.bean.DeviceBean;

/**
 * Created by Robin on 2019/4/18.
 * 419109715@qq.com 彬影
 */
public class BaseReq {

    protected DeviceBean deviceBean;

    public BaseReq(DeviceBean deviceBean) {
        this.deviceBean = deviceBean;
    }

    @Override
    public String toString() {
        return "BaseReq{" +
                "deviceBean=" + deviceBean +
                '}';
    }

    public DeviceBean getDeviceBean() {
        return deviceBean;
    }

    public void setDeviceBean(DeviceBean deviceBean) {
        this.deviceBean = deviceBean;
    }
}
