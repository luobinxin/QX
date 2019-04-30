package cn.com.startai.qxsdk.busi.socket.entity;

import cn.com.startai.qxsdk.busi.common.BaseResp;
import cn.com.startai.qxsdk.db.bean.DeviceBean;

/**
 * Created by Robin on 2019/4/17.
 * 419109715@qq.com 彬影
 */
public class RenameDeviceResp extends BaseResp {

    private DeviceBean deviceBean;
    private String newName;

    public RenameDeviceResp(int result, DeviceBean deviceBean, String newName) {
        super(result);
        this.deviceBean = deviceBean;
        this.newName = newName;
    }

    public RenameDeviceResp(DeviceBean deviceBean, String newName) {
        this.deviceBean = deviceBean;
        this.newName = newName;
    }

    @Override
    public String toString() {
        return "RenameDeviceReq{" +
                "deviceBean=" + deviceBean +
                ", newName='" + newName + '\'' +
                '}';
    }

    public DeviceBean getDeviceBean() {
        return deviceBean;
    }

    public void setDeviceBean(DeviceBean deviceBean) {
        this.deviceBean = deviceBean;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}
