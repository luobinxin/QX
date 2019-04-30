package cn.com.startai.qxsdk.busi.socket.entity;


import cn.com.startai.qxsdk.db.bean.DeviceBean;

/**
 * author: Guoqiang_Sun
 * date : 2018/9/30 0030
 * desc :
 */
public class UpdateVersionReq extends BaseReq {


    public UpdateVersionReq(DeviceBean deviceBean) {
        super(deviceBean);
    }
}
