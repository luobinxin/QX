package cn.com.startai.qxsdk.busi.common;

/**
 * Created by Robin on 2019/3/8.
 * 419109715@qq.com 彬影
 */
public class BindDeviceReq {

    private String sn;  //广域网填sn
    private String mac; //局域网绑定填mac
    private String pwd; //局域网绑定如果需要密码填 pwd

    public BindDeviceReq(String mac, String pwd) {
        this.mac = mac;
        this.pwd = pwd;
    }

    public BindDeviceReq(String sn) {
        this.sn = sn;
    }

    @Override
    public String toString() {
        return "BindDeviceReq{" +
                "sn='" + sn + '\'' +
                ", mac='" + mac + '\'' +
                ", pwd='" + pwd + '\'' +
                '}';
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
