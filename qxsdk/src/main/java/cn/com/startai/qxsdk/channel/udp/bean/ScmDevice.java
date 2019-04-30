package cn.com.startai.qxsdk.channel.udp.bean;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.com.startai.qxsdk.channel.BaseData;
import cn.com.startai.qxsdk.event.IOutput;
import cn.com.startai.qxsdk.global.LooperManager;
import cn.com.swain.baselib.log.Tlog;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/2 0002
 * desc :
 */
public class ScmDevice implements Heartbeat.OnHeartbeatCallBack {


    public void removeQueryHistory() {
        if (mWorkHandler != null && mWorkHandler.hasMessages(1)) {
            mWorkHandler.removeMessages(1);
        }
    }

    public void removeQueryHistoryCountResult() {
        if (mWorkHandler != null && mWorkHandler.hasMessages(0)) {
            mWorkHandler.removeMessages(0);
        }
    }


    public void onOutputDataToServer(BaseData data) {
        if (mWorkHandler != null) {
            Message message = mWorkHandler.obtainMessage(2, data);
            mWorkHandler.sendMessageDelayed(message, 1000 * 6);
        }
    }

    private boolean nightLightOn;

    public void putNightLightState(boolean on) {
        nightLightOn = on;
    }

    public boolean isNightLightOn() {
        return nightLightOn;
    }


    public interface OnScmCallBack {
        void onStartSendHeartbeat(ScmDevice mScmDevice);

        void onHeartbeatLose(String mac, int diff);

        void onDelaySend(int what, Object obj);
    }


    public static final String DEFAULT_MAC = "00:00:00:00:00:00";

    private String address;

    private ScmDevice.OnScmCallBack mScmCallBack;

    private RepeatMsg mRepeatMsg;


    private Handler mWorkHandler;

    ScmDevice(String address, final IOutput mResponse, final ScmDevice.OnScmCallBack mHeartbeatCallBack) {

        this.address = address;
        this.createTimestamp = System.currentTimeMillis();
        this.mScmCallBack = mHeartbeatCallBack;
        Looper mRepeatLooper = LooperManager.getInstance().getRepeatLooper();
        this.mHeartbeat = new Heartbeat(mRepeatLooper, mResponse, this, address);

        this.mRepeatMsg = new RepeatMsg(address, mRepeatLooper, mResponse);

        mWorkHandler = new Handler(LooperManager.getInstance().getWorkLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (mHeartbeatCallBack != null) {
                    mHeartbeatCallBack.onDelaySend(msg.what, msg.obj);
                }
            }
        };

    }

    public boolean send0;
    public boolean send1;

    private int conFailTimes;

    public final int getConFailTimes() {
        return conFailTimes;
    }

    private final void setConFailTimes(int conFailTimes) {
        this.conFailTimes = conFailTimes;
    }

    public void setConResult(boolean result) {
        if (!result) {
            int i = getConFailTimes();
            setConFailTimes(++i);
        } else {
            setConFailTimes(0);
            // 连接成功，默认为接收到心跳，预防上次接收心跳失败。
            onReceiveHeartbeat(true);
        }
    }


    private int token;

    public final int getToken() {
        return token;
    }

    public final void setToken(int token) {
        this.token = token;
        if (this.mHeartbeat != null) {
            this.mHeartbeat.setToken(token);
        }
    }

    public final void putIp(String ip) {
        if (mHeartbeat != null) {
            mHeartbeat.setCanSendHeartbeat(ip != null);
        }
    }

    public final void recordSendMsg(BaseData data, long timeOut) {
        if (mRepeatMsg != null) {
            mRepeatMsg.recordSendMsg(data, timeOut);
        }
    }

    public final void receiveOnePkg(int what, int seq) {
        if (mRepeatMsg != null) {
            mRepeatMsg.receiveOnePkg(what, seq);
        }
    }

    private long createTimestamp;

    private final Heartbeat mHeartbeat;

    private boolean hasUpdateTime;

    private boolean publish;

    private int sendHeartTimes;
//    private int recHeartTimes;


    private int requestRandom;

    public final void putRequestTokenRandom(int random) {
        this.requestRandom = random;
    }

    public final int getRequestRandom() {
        return requestRandom;
    }

    public final String getAddress() {
        return address;
    }

    public final boolean isUpdateScmTime() {
        return hasUpdateTime;
    }

    private int updateTimes = 0;

    public final void setIsUpdateTime(boolean flag) {
        if (flag) {
            if (++updateTimes >= 3) {
                hasUpdateTime = true;

            }
        }
    }

    public final boolean isPublish() {
        return publish;
    }

    public final void setIsPublish(boolean flag) {
        publish = flag;
    }

    @Override
    public void onStartSendHeartbeat(String mac) {
        ++sendHeartTimes;
        if (mScmCallBack != null) {
            mScmCallBack.onStartSendHeartbeat(this);
        }

        int diff = getLostHeartbeatTimes();

        if (diff > 2) {
            mHeartbeat.check(diff, 1000 * 3);
        }

    }

    @Override
    public void onCheckHeartbeat(String toID, int diff) {
        if (mScmCallBack != null) {
            if (diff <= getLostHeartbeatTimes()) {
                mScmCallBack.onHeartbeatLose(toID, diff);
            }
        }
    }

    public final void onReceiveHeartbeat(boolean result) {
        if (result) {
            sendHeartTimes = 0;
        } else {
            sendHeartTimes--;
        }
    }

    private int getLostHeartbeatTimes() {
        return sendHeartTimes;
    }

    public final void connected() {
        starHeartbeat();
    }

    public final void disconnected() {
        setIsPublish(false);
        stopHeartbeat();
    }

    public final void release() {
        disconnected();
        if (mRepeatMsg != null) {
            mRepeatMsg.removeCallbacksAndMessages(null);
        }
        if (mWorkHandler != null) {
            mWorkHandler.removeCallbacksAndMessages(null);
        }
    }

    public final void clearSensor() {

    }

    private boolean checkAddress() {
        return address != null && !address.equalsIgnoreCase(DEFAULT_MAC);
    }

    public final void starHeartbeat() {

        if (!checkAddress()) {
            Tlog.e("SocketScmManager", " starHeartbeat byte address unInvalid " + address);
            return;
        }

        this.sendHeartTimes = 0;

        if (mHeartbeat != null) {
            mHeartbeat.start();
        }
    }

    public final void stopHeartbeat() {
        this.sendHeartTimes = 0;
        if (mHeartbeat != null) {
            mHeartbeat.stop();
        }
    }


    @Override
    public String toString() {
        return "createTimestamp:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(createTimestamp));
    }

}
