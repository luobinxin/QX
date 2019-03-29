package cn.com.startai.qxsdk;

import android.app.Application;
import android.support.annotation.NonNull;

import cn.com.startai.qxsdk.busi.entity.Activate;
import cn.com.startai.qxsdk.busi.entity.Login;
import cn.com.startai.qxsdk.connect.BaseData;
import cn.com.startai.qxsdk.event.IOnCallListener;
import cn.com.startai.qxsdk.event.IQXBusi;
import cn.com.startai.qxsdk.event.IQXBusiResultListener;
import cn.com.startai.qxsdk.global.QXInitParam;

/**
 * Created by Robin on 2019/3/20.
 * 419109715@qq.com 彬影
 */
public class QX implements IQXBusi {

    private static QXBusiManager qxBusiManager;

    public static final String TAG = "QX";

    private QX() {
        qxBusiManager = QXBusiManager.getInstance();
    }

    private static QX instance;

    public static QX getInstance() {
        if (instance == null) {
            instance = new QX();
        }
        return instance;
    }

    private boolean isDebug;

    public Application getApp() {
        return qxBusiManager.getApp();
    }

    public void setDebug(boolean isDebug) {
        this.isDebug = isDebug;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public void init(@NonNull Application app, QXInitParam qxInitParam) {

        qxBusiManager.init(app, qxInitParam);
    }

    public void release() {
        qxBusiManager.release();
    }

    public void doSend(BaseData baseData, IOnCallListener listener) {
        qxBusiManager.doSend(baseData, listener);
    }


    public IQXBusi getQxBusi() {
        return qxBusiManager.getQxbusi();
    }

    public void addListener(IQXBusiResultListener listener) {
        qxBusiManager.addListener(listener);
    }

    public void removeListener(IQXBusiResultListener listener) {
        qxBusiManager.removeListener(listener);
    }

    /**
     * 局域网设备扫描
     *
     * @param timeMillims
     */
    @Override
    public void discovery(long timeMillims) {
        qxBusiManager.discovery(timeMillims);
    }

    /**
     * 停止局域网设备扫描
     */
    @Override
    public void stopDiscovery() {
        qxBusiManager.stopDiscovery();
    }

    /**
     * 登录
     *
     * @param req
     * @param callListener
     */
    @Override
    public void login(@NonNull Login.Req req, IOnCallListener callListener) {
        qxBusiManager.login(req, callListener);
    }

    /**
     * 帮第三方设备激活
     *
     * @param req
     * @param callListener
     */
    @Override
    public void hardwareActivate(Activate.Req req, IOnCallListener callListener) {
        qxBusiManager.hardwareActivate(req, callListener);
    }
}
