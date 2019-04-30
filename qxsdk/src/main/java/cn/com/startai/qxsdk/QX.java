package cn.com.startai.qxsdk;

import android.app.Application;
import android.support.annotation.NonNull;

import cn.com.startai.qxsdk.channel.mqtt.ServerConnectState;
import cn.com.startai.qxsdk.event.IQXBusiResultListener;
import cn.com.startai.qxsdk.global.QXInitParam;

/**
 * Created by Robin on 2019/3/20.
 * 419109715@qq.com 彬影
 */
public class QX {

    //将构造函数私有化
    private QX() {
    }

    public static QX getInstance() {
        return SingleTonHoulder.singleTonInstance;
    }

    //静态内部类
    public static class SingleTonHoulder {
        private static final QX singleTonInstance = new QX();
    }


    private QXBusiManager qxBusiManager;
    private QXUserManager qxUserManager;

    public QXBusiManager getQxBusiManager() {
        if (qxBusiManager == null) {
            qxBusiManager = new QXBusiManager();
        }
        return qxBusiManager;
    }

    public QXUserManager getQxUserManager() {
        if (qxUserManager == null) {
            qxUserManager = QXUserManager.getInstance();
        }
        return qxUserManager;
    }

    public void setQxBusiManager(QXBusiManager qxBusiManager) {
        this.qxBusiManager = qxBusiManager;
    }

    public static final String TAG = "QX";


    private boolean isDebug;

    public boolean isInit() {
        return getQxBusiManager().isInit();
    }

    public Application getApp() {

        return getQxBusiManager().getApp();
    }

    public void setDebug(boolean isDebug) {
        this.isDebug = isDebug;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public void init(@NonNull Application app, QXInitParam qxInitParam) {

        getQxBusiManager().init(app, qxInitParam);
    }

    public void release() {
        getQxBusiManager().release();

    }

    /**
     * 添加监听
     *
     * @param listener
     */
    public void setQXBusiListener(IQXBusiResultListener listener) {

        getQxBusiManager().setQXBusiListener(listener);
    }


    public ServerConnectState getServerConnectState() {

        return getQxBusiManager().getServerConnectState();
    }


}
