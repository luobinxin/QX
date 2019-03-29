package cn.com.startai.qx;

import android.app.Application;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import cn.com.startai.helper.TAndL;
import cn.com.startai.qx.event.MyListener;
import cn.com.startai.qxsdk.QX;
import cn.com.startai.qxsdk.connect.mqtt.event.IQxMqttListener;
import cn.com.startai.qxsdk.event.IQXListener;
import cn.com.startai.qxsdk.global.QXError;
import cn.com.startai.qxsdk.global.QXInitParam;

/**
 * Created by Robin on 2019/3/22.
 * 419109715@qq.com 彬影
 */
public class App extends Application {

    public static final String TAG = "App";


    String appid = "6e3788eedb60442c88b647bfaa1d285b";

    @Override
    public void onCreate() {
        super.onCreate();

        QX.getInstance().setDebug(true);
        QX.getInstance().addListener(new MyListener());
        QX.getInstance().init(this, new QXInitParam(appid, QXInitParam.CUSTOM_STARTAI, QXInitParam.PRODUCT_MUSIK));


    }

    public static void exitApp() {
        QX.getInstance().release();
        System.exit(0);
    }

}
