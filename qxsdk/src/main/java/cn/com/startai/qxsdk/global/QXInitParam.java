package cn.com.startai.qxsdk.global;

/**
 * Created by Robin on 2019/3/7.
 * 419109715@qq.com 彬影
 */
public class QXInitParam implements ParamConsts {


    public String appid;

    public Byte mProtocolVersion = VERSION_SEQ;
    public Byte mCustom;
    public Byte mProduct  ;

    public Boolean needCustomerFilter;//是否根据 mCustom mProduct 过滤设备 默认 true

    public QXInitParam(String appid) {
        this.appid = appid;
    }

    public QXInitParam(String appid, Byte mProtocolVersion, Byte mCustom, Byte mProduct, Boolean needCustomerFilter) {
        this.appid = appid;
        this.mProtocolVersion = mProtocolVersion;
        this.mCustom = mCustom;
        this.mProduct = mProduct;
        this.needCustomerFilter = needCustomerFilter;
    }

    public QXInitParam(String appid, Byte mCustom, Byte mProduct, Boolean needCustomerFilter) {
        this.appid = appid;
        this.mCustom = mCustom;
        this.mProduct = mProduct;
        this.needCustomerFilter = needCustomerFilter;
    }

    public QXInitParam(String appid, Byte mCustom, Byte mProduct) {
        this.appid = appid;
        this.mCustom = mCustom;
        this.mProduct = mProduct;
    }
}
