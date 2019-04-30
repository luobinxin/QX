package cn.com.startai.qxsdk.channel.udp.bean;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

import cn.com.startai.qxsdk.channel.udp.event.IControlWiFi;
import cn.com.startai.qxsdk.db.bean.DeviceBean;
import cn.com.startai.qxsdk.global.LooperManager;
import cn.com.swain.baselib.log.Tlog;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/10 0010
 * desc :
 */
public class ControlDevice {

    private RepeatHandler mRepeatHandler;
    private DeviceBean deviceBean;
    private IControlWiFi.IWiFiResultCallBack mResultCallBack;
    private String mac;

    private boolean canLanCom = false;

    private void setCanLanCom() {
        this.canLanCom = true;
    }

    private void setCanNotLanCom() {
        this.canLanCom = false;
    }

    public boolean canLanCom() {
        return canLanCom;
    }


    public ControlDevice(DeviceBean deviceBean, IControlWiFi.IWiFiResultCallBack mResultCallBack) {
        this.deviceBean = deviceBean;
        this.mac = deviceBean.getMac();
        this.mResultCallBack = mResultCallBack;
        this.mRepeatHandler = new RepeatHandler(this, LooperManager.getInstance().getRepeatLooper());
    }

    private static final int MSG_WHAT_CALL_JS_CON = 0x02;

    private boolean hasCallback = false;

    private void handleMessage(Message msg) {

        if (msg.what == MSG_WHAT_CALL_JS_CON) {
            if (mResultCallBack != null) {
                hasCallback = true;

                int state = (int) msg.obj;
                Tlog.e(TAG, "handleMessage lan connect " + (state == 1 ? "success" : "failed"));
                // msg.obj == 1 连接成功
                mResultCallBack.onResultConnectDevice(state, deviceBean);

            }
        }

    }

    public void receiveHeartbeat(boolean result) {
        if (result) {
            if (!canLanCom()) {
                Tlog.e(TAG, " receiveHeartbeat but canNotLanCom");
                resetSendConTokenTimes();
            }
        }
    }

    public void heartbeatLose(int times) {
        Tlog.e(TAG, " heartbeatLose " + times + " setCanNotLanCom() ");
        setCanNotLanCom();
        if (getSendConTokenTimes() > 3) {
            resetSendConTokenTimes();
        }
        resetConnectedTimes();
    }

    private void callbackConnState(int state, int delay) {
        if (mRepeatHandler.hasMessages(MSG_WHAT_CALL_JS_CON)) {
            mRepeatHandler.removeMessages(MSG_WHAT_CALL_JS_CON);
        }
        Message message = mRepeatHandler.obtainMessage(MSG_WHAT_CALL_JS_CON, state);
        mRepeatHandler.sendMessageDelayed(message, delay);
    }


    public void removeCallJsCon() {

        mRepeatHandler.removeMessages(MSG_WHAT_CALL_JS_CON);

    }

    public void release() {

        if (mRepeatHandler != null) {
            mRepeatHandler.removeCallbacksAndMessages(null);
        }

    }

    private int conTokenTimes = 0;

    private int getSendConTokenTimes() {
        return conTokenTimes;
    }

    private void resetSendConTokenTimes() {
        Tlog.d(TAG, " resetSendConTokenTimes() ");
        this.conTokenTimes = 0;
    }

    public void onNetworkStateChange() {
        Tlog.d(TAG, " onNetworkStateChange  setCanNotLanCom()");
        resetSendConTokenTimes();
        setCanNotLanCom();
        callbackConnState(0, 100);
    }

    public void onTokenInvalid(int token, String loginUserID) {
        Tlog.d(TAG, " onTokenInvalid() token " + token + " userID:" + loginUserID + " setCanNotLanCom() ");
        setCanNotLanCom();
        checkComModel(-1, loginUserID);
    }

    public void lanDeviceDiscovery(int token, String loginUserID) {
        Tlog.d(TAG, " lanDeviceDiscovery() token " + token + " userID:" + loginUserID + " canLanCom:" + canLanCom());
        if (!canLanCom()) {
            hasCallback = false;
            checkComModel(token, loginUserID);
        }
    }

    public void controlWiFiDevice(int token, String loginUserID, boolean lanDiscovery) {
        Tlog.d(TAG, " controlWiFiDevice() token " + token + " userID:" + loginUserID + " lanDiscovery:" + lanDiscovery + " isWanBind" + deviceBean.isWanBind());

        hasCallback = false;

        resetSendConTokenTimes();

        if (!lanDiscovery) {
            callbackConnState(0, 200);
        } else {
            checkComModel(token, loginUserID);
            callbackConnState(0, 1000 * 10);
        }

    }

    public void recontrolWiFiDevice(int token, String loginUserID, boolean lanDiscovery) {
        Tlog.d(TAG, " recontrolWiFiDevice() token " + token + " userID:" + loginUserID + " lanDiscovery:" + lanDiscovery + " isWanBind" + deviceBean.isWanBind());

        hasCallback = false;

        resetSendConTokenTimes();

        if (!lanDiscovery) {
            callbackConnState(0, 100);
        } else {
            if (canLanCom) {
                callbackConnState(1, 100);
            } else {
                checkComModel(token, loginUserID);
                callbackConnState(0, 1000 * 10);
            }
        }

    }

    public void disControl() {
        Tlog.e(TAG, " disControl " + mac + " setCanNotLanCom() ");
        removeCallJsCon();
        setCanNotLanCom();
        resetConnectedTimes();
        resetSendConTokenTimes();
        callbackConnState(0, 100);
    }

    public void onResponseToken(String loginUserID, int token) {

        Tlog.e(TAG, " onResponseToken() loginUserID :" + loginUserID
                + " token:" + Integer.toHexString(token));

        checkComModel(token, loginUserID);

    }

    private int responseConnectedTimes = 0;

    private void resetConnectedTimes() {
        Tlog.i(TAG, " resetConnectedTimes() ");
        this.responseConnectedTimes = 0;
    }

    public void responseConnected(boolean canLanCom) {
        Tlog.d(TAG, " responseConnected  canLanCom:" + canLanCom + " responseConnectedTimes:" + responseConnectedTimes);

        // 防止一直返回连接成功
        if (++responseConnectedTimes <= 6) {
            resetSendConTokenTimes();
        }

        removeCallJsCon();
        if (canLanCom) {
            setCanLanCom();
        }

        if (!hasCallback) {
            callbackConnState(canLanCom ? 1 : 0, 100);
        }
    }

    public void responseConnectedFail(String loginUserID) {
        Tlog.e(TAG, " responseConnected  canLanCom:" + canLanCom);
        checkComModel(-1, loginUserID);
    }

    // 检测通信模式
    private void checkComModel(int token, String loginUserID) {

        if (getSendConTokenTimes() > 5) {
            Tlog.e(TAG, " checkComModel() getSendConTokenTimes " + getSendConTokenTimes()
                    + " responseConnectedTimes:" + responseConnectedTimes + " return;");
            return;
        }


        if (token == -1 || token == 0) {
            Tlog.e(TAG, " checkComModel() NeedRequestToken ");

            if (mResultCallBack != null) {
                mResultCallBack.onResultNeedRequestToken(mac, loginUserID);
            }

        } else {
            Tlog.e(TAG, " checkComModel() canControlDevice token " + Integer.toHexString(token));

            conTokenTimes++;

            if (mResultCallBack != null) {
                mResultCallBack.onResultCanControlDevice(mac, loginUserID, token);
            }
        }

    }


    private static final class RepeatHandler extends Handler {
        private final WeakReference<ControlDevice> wr;

        RepeatHandler(ControlDevice mNetworkManager, Looper mLooper) {
            super(mLooper);
            wr = new WeakReference<>(mNetworkManager);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ControlDevice mNetworkManager;
            if ((mNetworkManager = wr.get()) != null) {
                mNetworkManager.handleMessage(msg);
            }

        }
    }


}
