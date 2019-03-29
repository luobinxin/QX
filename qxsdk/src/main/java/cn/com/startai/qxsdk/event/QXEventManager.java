package cn.com.startai.qxsdk.event;

import java.util.ArrayList;
import java.util.List;

import cn.com.startai.qxsdk.busi.entity.Activate;
import cn.com.startai.qxsdk.busi.entity.Login;
import cn.com.startai.qxsdk.busi.entity.UpdateDeviceInfo;
import cn.com.startai.qxsdk.db.bean.DeviceBean;
import cn.com.startai.qxsdk.global.QXError;

/**
 * Created by Robin on 2019/3/25.
 * 419109715@qq.com 彬影
 */
public class QXEventManager implements IQXBusiResultListener {

    private QXEventManager() {
    }

    private static QXEventManager instance;

    public static QXEventManager getInstance() {
        if (instance == null) {
            synchronized (QXEventManager.class) {
                if (instance == null) {
                    instance = new QXEventManager();
                }
            }
        }
        return instance;
    }


    protected List<IQXBusiResultListener> busiListenerList = new ArrayList<>();

    public void addQXBusiResultListener(IQXBusiResultListener listener) {
        synchronized (this) {
            if (null != listener) {
                if (null == this.busiListenerList) {
                    this.busiListenerList = new ArrayList<>();
                }
                this.busiListenerList.add(listener);
            }
        }
    }

    public void removeBusiResultListener(IQXBusiResultListener listener) {
        synchronized (this) {
            if (null != listener && null != this.busiListenerList && 0 < this.busiListenerList.size()) {
                this.busiListenerList.remove(listener);
            }
        }
    }


    /**
     * 更新设备信息
     *
     * @param resp
     */
    @Override
    public void onUpdateDeviceInfoResult(UpdateDeviceInfo.Resp resp) {
        if (busiListenerList != null) {
            for (final IQXBusiResultListener listener : busiListenerList) {
                listener.onUpdateDeviceInfoResult(resp);
            }
        }
    }

    /**
     * 登录结果
     *
     * @param resp
     */
    @Override
    public void onLoginResult(Login.Resp resp) {
        if (busiListenerList != null) {
            for (final IQXBusiResultListener listener : busiListenerList) {
                listener.onLoginResult(resp);
            }
        }
    }

    /**
     * 与服务器连接成功
     */
    @Override
    public void onServerConnected() {
        if (busiListenerList != null) {
            for (final IQXBusiResultListener listener : busiListenerList) {
                listener.onServerConnected();
            }
        }
    }

    /**
     * 与服务器断开连接|连接失败
     *
     * @param qxError
     */
    @Override
    public void onServerDisConnect(QXError qxError) {
        if (busiListenerList != null) {
            for (final IQXBusiResultListener listener : busiListenerList) {
                listener.onServerDisConnect(qxError);
            }
        }
    }

    /**
     * 重连接中
     */
    @Override
    public void onServerReConnecting() {
        if (busiListenerList != null) {
            for (final IQXBusiResultListener listener : busiListenerList) {
                listener.onServerReConnecting();
            }
        }
    }

    /**
     * 发现一台设备（局域网）
     *
     * @param deviceBean
     */
    @Override
    public void onDiscoveryResult(DeviceBean deviceBean) {
        if (busiListenerList != null) {
            for (final IQXBusiResultListener listener : busiListenerList) {
                listener.onDiscoveryResult(deviceBean);
            }
        }
    }

    /**
     * 开始发现（局域网）
     */
    @Override
    public void onDiscoveryStart() {
        if (busiListenerList != null) {
            for (final IQXBusiResultListener listener : busiListenerList) {
                listener.onDiscoveryStart();
            }
        }
    }

    /**
     * 停止发现（局域网）
     */
    @Override
    public void onDiscoveryStop() {
        if (busiListenerList != null) {
            for (final IQXBusiResultListener listener : busiListenerList) {
                listener.onDiscoveryStop();
            }
        }
    }

    /**
     * 第三方硬件激活结果
     *
     * @param resp
     */
    @Override
    public void onHardwareActivateResult(Activate.Resp resp) {
        if (busiListenerList != null) {
            for (final IQXBusiResultListener listener : busiListenerList) {
                listener.onHardwareActivateResult(resp);
            }
        }
    }


}