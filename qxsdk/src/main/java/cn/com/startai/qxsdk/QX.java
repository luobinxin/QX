package cn.com.startai.qxsdk;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;

import cn.com.startai.qxsdk.connect.ble.IQXBLE;
import cn.com.startai.qxsdk.connect.ble.QXBleImpl;
import cn.com.startai.qxsdk.connect.mqtt.IQXMqtt;
import cn.com.startai.qxsdk.connect.mqtt.client.QXMqttImpl;
import cn.com.startai.qxsdk.connect.udp.IQXUDP;
import cn.com.startai.qxsdk.connect.udp.QXUDPImpl;
import cn.com.startai.qxsdk.receiver.QXNetworkReceiver;

/**
 * Created by Robin on 2019/3/20.
 * 419109715@qq.com 彬影
 */
public class QX {

    private static boolean isDebug;
    private static IQXUDP qxUdp;
    private static IQXMqtt qxMqtt;
    private static IQXBLE qxBle;
    private static Application app;
    private static QXNetworkReceiver networkReceiver;

    public static QXNetworkReceiver getNetworkReceiver() {
        return networkReceiver;
    }

    public static void setNetworkReceiver(QXNetworkReceiver networkReceiver) {
        QX.networkReceiver = networkReceiver;
    }

    public static boolean isIsDebug() {
        return isDebug;
    }

    public static void setIsDebug(boolean isDebug) {
        QX.isDebug = isDebug;
    }

    public static IQXUDP getQxUdp() {
        return qxUdp;
    }

    public static void setQxUdp(IQXUDP qxUdp) {
        QX.qxUdp = qxUdp;
    }

    public static IQXMqtt getQxMqtt() {
        return qxMqtt;
    }

    public static void setQxMqtt(IQXMqtt qxMqtt) {
        QX.qxMqtt = qxMqtt;
    }

    public static IQXBLE getQxBle() {
        return qxBle;
    }

    public static void setQxBle(IQXBLE qxBle) {
        QX.qxBle = qxBle;
    }

    public static Application getApp() {
        return app;
    }

    public static void setApp(Application app) {
        QX.app = app;
    }

    public static void initUdp(Application app) {
        setApp(app);
        QXUDPImpl.registerInstance();
    }

    public static void initMqtt(@NonNull Application app, @NonNull String appid) {
        setApp(app);
        QXMqttImpl.registerInstance(appid);
    }

    public static void initBle(Application app) {
        setApp(app);
        QXBleImpl.registerInstance();
    }

    public static void init(@NonNull Application app, @NonNull String appid) {
        setApp(app);
        registerNetworkReceiver();
        initBle(app);
        initMqtt(app, appid);
        initUdp(app);
    }

    public static void release() {
        if (getApp() != null) {
            getQxBle().release();
            getQxMqtt().release();
            getQxUdp().release();
            unRegisterNetworkReceiver();
            setApp(null);
            networkReceiver = null;
        }
    }

    static void registerNetworkReceiver() {

        networkReceiver = new QXNetworkReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        Application app = QX.getApp();
        if (app != null && networkReceiver != null) {
            app.registerReceiver(networkReceiver, filter);
        }

    }

    static void unRegisterNetworkReceiver() {
        if (networkReceiver != null && QX.getApp() != null) {
            QX.getApp().unregisterReceiver(networkReceiver);
        }
    }

}
