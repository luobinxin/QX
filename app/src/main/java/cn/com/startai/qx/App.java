package cn.com.startai.qx;

import android.app.Application;

import cn.com.startai.helper.TAndL;
import cn.com.startai.qxsdk.QX;
import cn.com.startai.qxsdk.connect.mqtt.event.IQxMqttListener;
import cn.com.startai.qxsdk.event.IQXListener;
import cn.com.startai.qxsdk.global.QXError;

/**
 * Created by Robin on 2019/3/22.
 * 419109715@qq.com 彬影
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        QX.setIsDebug(true);
        String appid = "12312654631231";
        QX.init(this,appid);

        QX.getQxMqtt().setListener(new IQXListener() {

            @Override
            public void onMqttConnected() {
                TAndL.TL(getApplicationContext(), "连接成功");

            }

            @Override
            public void onMqttDisconnected(QXError qxError) {
                TAndL.TL(getApplicationContext(), "连接断开");
            }

            @Override
            public void onMqttReconnecting() {
                TAndL.TL(getApplicationContext(), "正在重连");
            }

            @Override
            public void onMessageArrived() {
                TAndL.TL(getApplicationContext(), "onMessageArrived");
            }
        });

        QX.getQxMqtt().publish();


    }

    public static void exitApp(){
        System.exit(0);
    }

}
