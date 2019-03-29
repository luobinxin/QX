package cn.com.startai.qxsdk.connect.udp.task.system;

import cn.com.startai.qxsdk.connect.udp.SocketSecureKey;
import cn.com.startai.qxsdk.connect.udp.task.OnUdpTaskCallBack;
import cn.com.swain.baselib.log.Tlog;
import cn.com.swain.support.protocolEngine.datagram.SocketDataArray;
import cn.com.swain.support.protocolEngine.task.SocketResponseTask;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/26 0026
 * desc :
 */
public class SleepReceiveTask extends SocketResponseTask {

    private OnUdpTaskCallBack mTaskCallBack;

    public SleepReceiveTask(OnUdpTaskCallBack mTaskCallBack) {
        Tlog.e(TAG, " new SleepReceiveTask() ");
        this.mTaskCallBack = mTaskCallBack;
    }


    @Override
    protected void doTask(SocketDataArray mSocketDataArray) {

        byte[] protocolParams = mSocketDataArray.getProtocolParams();

        if (protocolParams == null || protocolParams.length < 1) {
            Tlog.e(TAG, " SleepReceiveTask error:" + mSocketDataArray.toString());
            if (mTaskCallBack != null) {
                mTaskCallBack.onSleepResult(false, mSocketDataArray.getID());
            }
            return;
        }

        boolean result = SocketSecureKey.Util.resultIsOk(protocolParams[0]);

        Tlog.e(TAG, " SleepReceiveTask result:" + result + " params:" + Integer.toHexString(protocolParams[0]));

        if (mTaskCallBack != null) {
            mTaskCallBack.onSleepResult(result, mSocketDataArray.getID());
        }

    }
}
