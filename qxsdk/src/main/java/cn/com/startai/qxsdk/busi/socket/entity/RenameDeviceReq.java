package cn.com.startai.qxsdk.busi.socket.entity;

import cn.com.startai.qxsdk.db.bean.DeviceBean;

/**
 * Created by Robin on 2019/4/17.
 * 419109715@qq.com 彬影
 */
public class RenameDeviceReq extends BaseReq {

    private String newName;

    public RenameDeviceReq(DeviceBean deviceBean, String newName) {
        super(deviceBean);
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
