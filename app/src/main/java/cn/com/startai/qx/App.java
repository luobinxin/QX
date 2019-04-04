package cn.com.startai.qx;

import android.app.Application;

import com.facebook.stetho.Stetho;

import cn.com.startai.fssdk.StartaiUploaderManager;
import cn.com.startai.fssdk.UploadInitParam;
import cn.com.startai.qx.event.MyListener;
import cn.com.startai.qx.utils.DemoSPUtils;
import cn.com.startai.qxsdk.QX;
import cn.com.startai.qxsdk.connect.mqtt.client.QXMqttConfig;

/**
 * Created by Robin on 2019/3/22.
 * 419109715@qq.com 彬影
 */
public class App extends Application {

    public static final String TAG = "App";

    String appid = "6e3788eedb60442c88b647bfaa1d285b";
    MyListener listener = new MyListener();

    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);
        QXMqttConfig.HOST_DEBUG = "ssl://192.168.16.210";
//        QXMqttConfig.HOST_DEBUG = QXMqttConfig.HOST_US;
        DemoSPUtils.getInstance().init(this);
        QX.getInstance().setDebug(true);
        QX.getInstance().setQXBusiListener(listener);

        StartaiUploaderManager.getInstance().init(this, new UploadInitParam());
    }
}
