package cn.com.startai.qxsdk.busi.common;

/**
 * Created by Robin on 2019/3/8.
 * 419109715@qq.com 彬影
 */
public class UnBindDeviceReq {

    private String sn;


    public UnBindDeviceReq(String sn) {
        this.sn = sn;
    }

    public String toString() {
        return "UnBindDeviceReq{" +
                "sn='" + sn + '\'' +
                '}';
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

}
