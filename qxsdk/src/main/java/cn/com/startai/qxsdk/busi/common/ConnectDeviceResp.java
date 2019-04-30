package cn.com.startai.qxsdk.busi.common;


import cn.com.startai.qxsdk.db.bean.DeviceBean;

/**
 * Created by Robin on 2019/3/7.
 * 419109715@qq.com 彬影
 */
public class ConnectDeviceResp extends BaseResp {

    private DeviceBean deviceBean;


    public ConnectDeviceResp(int result, DeviceBean deviceBean) {
        super(result);
        this.deviceBean = deviceBean;
    }

    public ConnectDeviceResp(int result, DeviceBean deviceBean, String errorCode) {
        super(result, errorCode);
        this.deviceBean = deviceBean;
    }

    @Override
    public String toString() {
        return "ConnectDeviceResp{" +
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
