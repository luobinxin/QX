package cn.com.startai.qxsdk.busi.socket.task;

import cn.com.startai.qxsdk.busi.socket.ISocketTaskCallback;
import cn.com.startai.qxsdk.channel.udp.SocketSecureKey;
import cn.com.swain.baselib.log.Tlog;
import cn.com.swain.support.protocolEngine.datagram.SocketDataArray;
import cn.com.swain.support.protocolEngine.task.SocketResponseTask;

/**
 * author: Guoqiang_Sun
 * date : 2018/6/1 0001
 * desc :
 */
public class RenameReceiveTask extends SocketResponseTask {

    private ISocketTaskCallback mOnTaskCallBack;

    public RenameReceiveTask(ISocketTaskCallback mOnTaskCallBack) {
        this.mOnTaskCallBack = mOnTaskCallBack;
        Tlog.e(TAG, " new RenameReceiveTask ");
    }


    @Override
    protected void doTask(SocketDataArray mSocketDataArray) {

        byte[] protocolParams = mSocketDataArray.getProtocolParams();

        if (protocolParams.length < 33) {
            if (mOnTaskCallBack != null) {
                mOnTaskCallBack.onDeviceRenameResult(mSocketDataArray.getID(), false, "UNKNOWN");
            }
            return;
        }

        boolean result = SocketSecureKey.Util.resultIsOk(protocolParams[0]);

        String deviceName;
        deviceName = new String(protocolParams, 1, 32);
        deviceName = deviceName.trim().replaceAll("\\s*", "");
        Tlog.v(TAG, " deviceName :" + deviceName);

        if (mOnTaskCallBack != null) {
            mOnTaskCallBack.onDeviceRenameResult(mSocketDataArray.getID(), result, deviceName);
        }

    }
}
