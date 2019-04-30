package cn.com.startai.qxsdk.channel.udp.bean;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import cn.com.startai.qxsdk.channel.BaseData;
import cn.com.startai.qxsdk.event.IOutput;
import cn.com.swain.support.protocolEngine.Repeat.RepeatMsgModel;
import cn.com.swain.support.protocolEngine.pack.ResponseData;
import cn.com.swain.baselib.log.Tlog;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/9 0009
 * desc :
 */
public class RepeatMsg extends Handler {


    private final String mac;
    private final IOutput mResponse;
    private static final int TIME_OUT = 1000 * 6;

    public RepeatMsg(String mac, Looper mLooper, IOutput mResponse) {
        super(mLooper);
        this.mResponse = mResponse;
        this.mac = mac;
    }

    public void recordSendMsg(BaseData baseData) {
        recordSendMsg(baseData, TIME_OUT);
    }

    /**
     * @param data    {@link ResponseData}
     * @param timeOut 超时重发时间
     */
    public void recordSendMsg(BaseData data, long timeOut) {

        data.getRepeatMsgModel();

        RepeatMsgModel repeatMsgModel = data.getRepeatMsgModel();
        int msgWhat = repeatMsgModel.getMsgWhat();
        int msgSeq = repeatMsgModel.getMsgSeq();

        if (hasMessages(msgWhat)) {
            removeMessages(msgWhat);
        }

        Message message = obtainMessage(msgWhat, msgSeq, msgSeq, data);
        sendMessageDelayed(message, timeOut);
    }

    private int lastSeq;

    public void receiveOnePkg(int what, int seq) {

        if (hasMessages(what)) {
            removeMessages(what);
        }

        this.lastSeq = seq;

    }

    public void handleMessage(Message msg) {
        if (Tlog.isDebug()) {
            Tlog.d(TAG, " handleMessage timeout what:" + Integer.toHexString(msg.what) + " seq:" + msg.arg1);
        }

        BaseData data = (BaseData) msg.obj;

        if (data != null) {
            if (Tlog.isDebug()) {
                Tlog.d(TAG, " mRepeatHandler repeatSendMsg:" + data.toString());
            }

            if (data.getRepeatMsgModel().isNeedRepeatSend()) {

                data.getRepeatMsgModel().setRepeatOnce();

                if (mResponse != null) {
                    mResponse.doSend(data, null);
                }

            }

        }

    }


}
