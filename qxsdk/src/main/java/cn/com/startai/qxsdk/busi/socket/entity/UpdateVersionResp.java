package cn.com.startai.qxsdk.busi.socket.entity;


import cn.com.startai.qxsdk.busi.common.BaseResp;
import cn.com.startai.qxsdk.channel.udp.SocketSecureKey;
import cn.com.startai.qxsdk.db.bean.DeviceBean;

/**
 * author: Guoqiang_Sun
 * date : 2018/9/30 0030
 * desc :
 */
public class UpdateVersionResp extends BaseResp {

    public DeviceBean deviceBean;
    public byte action;
    public int progress;



    public boolean isQueryVersionAction() {
        return SocketSecureKey.Util.isQueryVersionAction(action);
    }

    public boolean isUpdateVersionAction() {
        return SocketSecureKey.Util.isUpdateModel(action);
    }


    public byte curVersionMain;
    public byte curVersionSub;

    public double getDoubleCurVersion() {
        int m = (curVersionMain & 0xFF);
        int s = (curVersionSub & 0xFF);
        String v = m + "." + s;
        double v1;
        try {
            v1 = Double.parseDouble(v);
        } catch (Exception e) {
            v1 = curVersion;
        }
        return v1;
    }

    public int curVersion;

    public int newVersion;

    public byte newVersionMain;
    public byte newVersionSub;


    public double getDoubleNewVersion() {
        int m = (newVersionMain & 0xFF);
        int s = (newVersionSub & 0xFF);
        String v = m + "." + s;
        double v1;
        try {
            v1 = Double.parseDouble(v);
        } catch (Exception e) {
            v1 = newVersion;
        }
        return v1;
    }

    public static void main(String[] args) {
        UpdateVersionResp mVersion = new UpdateVersionResp();
        mVersion.curVersionMain = 0x04;
        mVersion.curVersionSub = 0x00;
        System.out.println(mVersion.getDoubleCurVersion());
        System.out.println(String.valueOf(mVersion.getDoubleCurVersion()));
    }

    @Override
    public String toString() {
        return "UpdateVersionReq{" +
                "deviceBean=" + deviceBean +
                ", action=" + action +
                ", progress=" + progress +
                ", curVersionMain=" + curVersionMain +
                ", curVersionSub=" + curVersionSub +
                ", curVersion=" + curVersion +
                ", newVersion=" + newVersion +
                ", newVersionMain=" + newVersionMain +
                ", newVersionSub=" + newVersionSub +
                '}';
    }

    public DeviceBean getDeviceBean() {
        return deviceBean;
    }

    public void setDeviceBean(DeviceBean deviceBean) {
        this.deviceBean = deviceBean;
    }

}
