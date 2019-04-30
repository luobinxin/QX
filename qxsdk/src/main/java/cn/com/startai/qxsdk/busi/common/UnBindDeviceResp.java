package cn.com.startai.qxsdk.busi.common;

/**
 * Created by Robin on 2019/3/8.
 * 419109715@qq.com 彬影
 */
public class UnBindDeviceResp extends BaseResp {

    private String sn;

    public UnBindDeviceResp() {
    }

    public UnBindDeviceResp(int result, String sn ) {
        super(result);
        this.sn = sn;
    }

    public UnBindDeviceResp(int result, String errorCode, String errorMsg, String sn) {
        super(result, errorCode, errorMsg);
        this.sn = sn;
    }

    @Override
    public String toString() {
        return "UnBindDeviceResp{" +
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
