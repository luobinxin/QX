package cn.com.startai.qx;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.tencent.bugly.crashreport.CrashReport;

import cn.com.startai.fssdk.StartaiUploaderManager;
import cn.com.startai.fssdk.UploadInitParam;
import cn.com.startai.qx.event.MyListener;
import cn.com.startai.qx.utils.DemoSPUtils;
import cn.com.startai.qxsdk.QX;
import cn.com.startai.qxsdk.busi.socket.SocketBusiManager;
import cn.com.startai.qxsdk.channel.mqtt.client.QXMqttConfig;

/**
 * Created by Robin on 2019/3/22.
 * 419109715@qq.com 彬影
 */
public class App extends Application {

    public static final String TAG = "App";

    String wxsecret = "6d4ff86ac6a63d07ca009956a013d017";
    String wxappid = "wx06b791bccc38c10b";

    String appid = "6e3788eedb60442c88b647bfaa1d285b";
    private static MyListener listener;

    @Override
    public void onCreate() {
        super.onCreate();
        listener = new MyListener();
        Stetho.initializeWithDefaults(this);
//        QXMqttConfig.HOST_DEBUG = "ssl://192.168.16.210:8883";
        QXMqttConfig.HOST_DEBUG = QXMqttConfig.HOST_US;

//        QX.getInstance().setQXBusiListener(listener);
        SocketBusiManager chargerBusiManager = SocketBusiManager.getInstance();
        chargerBusiManager.setSocketBusiListener(listener);
        QX.getInstance().setQxBusiManager(chargerBusiManager);
        QX.getInstance().setDebug(true);


        StartaiUploaderManager.getInstance().init(this, new UploadInitParam());
        DemoSPUtils.getInstance().init(this);
        CrashReport.initCrashReport(getApplicationContext(), "33a635b0f7", false);
    }

    public static MyListener getListener() {
        return listener;
    }

    public static void setListener(MyListener listener) {
        App.listener = listener;
    }
}





