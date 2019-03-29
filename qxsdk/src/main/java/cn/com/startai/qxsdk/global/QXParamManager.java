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

    private byte mProtocolVersion = VERSION_SEQ;
    private byte mCustom = CUSTOM_STARTAI;
    private byte mProduct = PRODUCT_MUSIK;
    private boolean needCustomerFilter = true;


    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public byte getmProtocolVersion() {
        return mProtocolVersion;
    }

    public void setmProtocolVersion(byte mProtocolVersion) {
        this.mProtocolVersion = mProtocolVersion;
    }

    public byte getmCustom() {
        return mCustom;
    }

    public void setmCustom(byte mCustom) {
        this.mCustom = mCustom;
    }

    public byte getmProduct() {
        return mProduct;
    }

    public void setmProduct(byte mProduct) {
        this.mProduct = mProduct;
    }

    public boolean isNeedCustomerFilter() {
        return needCustomerFilter;
    }

    public void setNeedCustomerFilter(boolean needCustomerFilter) {
        this.needCustomerFilter = needCustomerFilter;
    }

}
