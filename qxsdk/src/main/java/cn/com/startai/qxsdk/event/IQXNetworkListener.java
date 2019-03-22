package cn.com.startai.qxsdk.event;

/**
 * Created by Robin on 2019/3/22.
 * 419109715@qq.com 彬影
 */
public interface IQXNetworkListener {

    void onWifiConnected();

    void onMobileConnected();

    void onEthernetConnected();

    void onUnkownNetwork();

    void onNetworkStateChange();

}
