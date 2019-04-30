package cn.com.startai.qxsdk.busi.socket.entity;

import cn.com.startai.qxsdk.busi.common.BaseResp;
import cn.com.startai.qxsdk.db.bean.DeviceBean;

/**
 * Created by Robin on 2019/4/18.
 * 419109715@qq.com 彬影
 */
public class RecoveryResp extends BaseResp {
    private DeviceBean deviceBean;

    public RecoveryResp(int result, DeviceBean deviceBean) {
        super(result);
        this.deviceBean = deviceBean;
    }

    @Override
    public String toString() {
        return "RecoveryReq{" +
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
