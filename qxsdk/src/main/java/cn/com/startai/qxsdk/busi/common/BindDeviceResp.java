package cn.com.startai.qxsdk.busi.common;

import cn.com.startai.qxsdk.db.bean.DeviceBean;

/**
 * Created by Robin on 2019/3/8.
 * 419109715@qq.com 彬影
 */
public class BindDeviceResp extends BaseResp {

    private DeviceBean deviceBean;

    public BindDeviceResp(int result, DeviceBean deviceBean) {
        super(result);
        this.deviceBean = deviceBean;
    }

    public BindDeviceResp(int result, String errorCode, String errorMsg, DeviceBean deviceBean) {
        super(result, errorCode, errorMsg);
        this.deviceBean = deviceBean;
    }

    @Override
    public String toString() {
        return "BindDeviceResp{" +
                "deviceBean=" + deviceBean +
                ", errcode='" + errcode + '\'' +
                ", errmsg='" + errmsg + '\'' +
                '}';
    }

    public DeviceBean getDeviceBean() {
        return deviceBean;
    }

    public void setDeviceBean(DeviceBean deviceBean) {
        this.deviceBean = deviceBean;
    }
}

