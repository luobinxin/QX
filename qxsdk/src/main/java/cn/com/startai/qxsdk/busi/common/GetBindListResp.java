package cn.com.startai.qxsdk.busi.common;

import java.util.List;

import cn.com.startai.qxsdk.db.bean.DeviceBean;

/**
 * Created by Robin on 2019/4/15.
 * 419109715@qq.com 彬影
 */
public class GetBindListResp extends BaseResp {

    private List<DeviceBean> deviceBeanList;
    private boolean isFromLocal; //true 表示 从本地加载 默认从云端加载

    public GetBindListResp(int result, List<DeviceBean> deviceBeanList) {
        super(result);
        this.deviceBeanList = deviceBeanList;
    }

    public GetBindListResp(int result, String errorCode, String errorMsg) {
        super(result, errorCode, errorMsg);
    }

    public boolean isFromLocal() {
        return isFromLocal;
    }

    public void setFromLocal(boolean fromLocal) {
        isFromLocal = fromLocal;
    }

    @Override
    public String toString() {
        return "GetBindListResp{" +
                "deviceBeanList=" + deviceBeanList +
                ", errcode='" + errcode + '\'' +
                ", errmsg='" + errmsg + '\'' +
                '}';
    }

    public List<DeviceBean> getDeviceBeanList() {
        return deviceBeanList;
    }

    public void setDeviceBeanList(List<DeviceBean> deviceBeanList) {
        this.deviceBeanList = deviceBeanList;
    }
}
