package cn.com.startai.qxsdk.channel.udp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.net.InetAddress;

import cn.com.startai.qxsdk.channel.BaseData;
import cn.com.startai.qxsdk.channel.udp.task.OnUdpTaskCallBack;
import cn.com.startai.qxsdk.utils.network.QXNetworkManager;
import cn.com.startai.qxsdk.utils.QXLog;
import cn.com.swain.baselib.log.Tlog;
import cn.com.swain.baselib.util.IpUtil;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * author: Guoqiang_Sun
 * date : 2018/7/11 0011
 * desc :
 */
public class BroadcastDiscoveryUtil {

    private final IQXUDP output;
    private final BroadcastHandler mBroadcastHandler;
    private final OnUdpTaskCallBack onUdpTaskCallBack;
    private Context context;

    public BroadcastDiscoveryUtil(Looper mWorkLooper, IQXUDP output, OnUdpTaskCallBack onUdpTaskCallBack, Context context) {
        this.context = context;
        this.output = output;
        this.onUdpTaskCallBack = onUdpTaskCallBack;
        this.mBroadcastHandler = new BroadcastHandler(mWorkLooper, this);
    }

    public void discoveryDevice(String userID) {
        String networkType = QXNetworkManager.getInstance().getNetworkType();
        if (!QXNetworkManager.NETWORKTYPE_WIFI.equals(networkType)) {
            QXLog.e(TAG, "discovery error : networkType is not 'WIFI' ");
            return;
        }

        byte[] bytes = userID != null ? userID.getBytes() : null;
        BaseData mResponseData = QXUdpDataCreater.getInstance().getDiscoveryDevice("255.255.255.255", bytes);
        InetAddress broadcastAddress = IpUtil.getBroadcastAddress(context);

        mResponseData.setIp(broadcastAddress.getHostAddress());
        mResponseData.setPort(9222);

        output.broadcast(mResponseData);

    }

    private boolean start = false;
    private long mTimes = 0;
    private long requestTimes = 0;

    public void startDiscovery(String userID, long millisecond) {

        long requstTimeTemp = 0;
        if (millisecond % PERIOD == 0) {
            requstTimeTemp = millisecond / PERIOD;
        } else {
            requstTimeTemp = millisecond / PERIOD + 1;
        }

        if (onUdpTaskCallBack != null) {
            onUdpTaskCallBack.onDiscoveryStart();
        }
        start = true;
        if (mBroadcastHandler.hasMessages(MSG_WHAT_SEND)) {
            mBroadcastHandler.removeMessages(MSG_WHAT_SEND);
        }

        //取当前剩余次数 跟 请求次数的最大值作为当前请求次数
        if (this.mTimes < requstTimeTemp) {
            this.mTimes = requstTimeTemp;
            this.requestTimes = requstTimeTemp;
        }


        Tlog.v("BroadcastDiscoveryUtil", " startDiscovery() " + userID + " times:" + mTimes);

        mBroadcastHandler.obtainMessage(MSG_WHAT_SEND, userID).sendToTarget();

    }

    private static final long PERIOD = 3000;

    public void stopDiscovery() {
        this.start = false;
        this.mTimes = 0;
        if (mBroadcastHandler.hasMessages(MSG_WHAT_SEND)) {
            mBroadcastHandler.removeMessages(MSG_WHAT_SEND);
        }
        mBroadcastHandler.removeCallbacksAndMessages(null);
        if (onUdpTaskCallBack != null) {

            onUdpTaskCallBack.onDiscoveryStop();
        }


    }


    private static final int MSG_WHAT_SEND = 0x01;

    private void handleMessage(Message msg) {

        Log.d(TAG, " remain discovery times " + mTimes + "/" + requestTimes);
        String userID = (String) msg.obj;
        discoveryDevice(userID);

        mTimes--;

        if (start && mTimes > 0) {
            Message message = mBroadcastHandler.obtainMessage(MSG_WHAT_SEND, userID);
            mBroadcastHandler.sendMessageDelayed(message, PERIOD);
        }

        if (mTimes <= 0) {
            if (onUdpTaskCallBack != null) {
                onUdpTaskCallBack.onDiscoveryStop();
            }
        }
    }

    private static class BroadcastHandler extends Handler {
        private final WeakReference<BroadcastDiscoveryUtil> wr;

        public BroadcastHandler(Looper mLooper, BroadcastDiscoveryUtil mBroadcastDiscoveryUtil) {
            super(mLooper);
            wr = new WeakReference<>(mBroadcastDiscoveryUtil);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BroadcastDiscoveryUtil broadcastDiscoveryUtil;

            if (wr != null && (broadcastDiscoveryUtil = wr.get()) != null) {
                broadcastDiscoveryUtil.handleMessage(msg);
            }
        }
    }


}
