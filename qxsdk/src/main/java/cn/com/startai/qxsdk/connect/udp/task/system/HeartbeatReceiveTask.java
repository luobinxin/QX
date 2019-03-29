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
public class HeartbeatReceiveTask extends SocketResponseTask {

    private OnUdpTaskCallBack mCallBack;

    public HeartbeatReceiveTask(OnUdpTaskCallBack mCallBack) {
        Tlog.e(TAG, " new HeartbeatReceiveTask() ");
        this.mCallBack = mCallBack;
    }

    @Override
    protected void doTask(SocketDataArray mSocketDataArray) {

        byte[] protocolParams = mSocketDataArray.getProtocolParams();

        if (protocolParams == null) {
            Tlog.e(TAG, " protocolParams == null");
            return;
        }

        Tlog.v(TAG, "Heartbeat result:" + SocketSecureKey.Util.resultIsOk(protocolParams[0]) + " -value:" + protocolParams[0]);

        if (mCallBack != null) {
            mCallBack.onHeartbeatResult(mSocketDataArray.getID(), SocketSecureKey.Util.resultIsOk(protocolParams[0]));

            if (SocketSecureKey.Util.resultTokenInvalid(protocolParams[0])) {
                mCallBack.onTokenInvalid(mSocketDataArray.getID());
            }

        }

    }
}
