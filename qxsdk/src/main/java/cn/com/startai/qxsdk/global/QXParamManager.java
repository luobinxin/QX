package cn.com.startai.qxsdk.global;


/**
 * Created by Robin on 2019/3/7.
 * 419109715@qq.com 彬影
 */
public class QXParamManager implements ParamConsts {


    private static QXParamManager instance;

    private QXParamManager() {
    }

    public static QXParamManager getInstance() {
        if (instance == null) {
            instance = new QXParamManager();
        }
        return instance;
    }

    private String appid;

    private Byte mProtocolVersion;
    private Byte mCustom;
    private Byte mProduct;
    private boolean needCustomerFilter = true;


    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public Byte getmProtocolVersion() {
        return mProtocolVersion;
    }

    public void setmProtocolVersion(Byte mProtocolVersion) {
        this.mProtocolVersion = mProtocolVersion;
    }

    public Byte getmCustom() {
        return mCustom;
    }

    public void setmCustom(Byte mCustom) {
        this.mCustom = mCustom;
    }

    public Byte getmProduct() {
        return mProduct;
    }

    public void setmProduct(Byte mProduct) {
        this.mProduct = mProduct;
    }

    public boolean isUdpParamInit() {
        return mProtocolVersion != null && mCustom != null && mProduct != null;
    }


    public boolean isNeedCustomerFilter() {
        return needCustomerFilter;
    }

    public void setNeedCustomerFilter(boolean needCustomerFilter) {
        this.needCustomerFilter = needCustomerFilter;
    }

}
