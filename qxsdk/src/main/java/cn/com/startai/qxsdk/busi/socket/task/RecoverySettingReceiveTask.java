package cn.com.startai.qxsdk.busi.socket.task;


import cn.com.startai.qxsdk.busi.socket.ISocketTaskCallback;
import cn.com.startai.qxsdk.channel.udp.SocketSecureKey;
import cn.com.swain.baselib.log.Tlog;
import cn.com.swain.support.protocolEngine.datagram.SocketDataArray;
import cn.com.swain.support.protocolEngine.task.SocketResponseTask;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/26 0026
 * desc :
 */
public class RecoverySettingReceiveTask extends SocketResponseTask {

    private ISocketTaskCallback mTaskCallBack;

    public RecoverySettingReceiveTask(ISocketTaskCallback mTaskCallBack) {
        Tlog.e(TAG, " new RecoverySettingReceiveTask() ");
        this.mTaskCallBack = mTaskCallBack;
    }


    @Override
    protected void doTask(SocketDataArray mSocketDataArray) {

        byte[] protocolParams = mSocketDataArray.getProtocolParams();

        if (protocolParams == null || protocolParams.length < 1) {
            Tlog.e(TAG, " protocolParams == null");
            if (mTaskCallBack != null) {
                mTaskCallBack.onSettingRecoveryResult(mSocketDataArray.getID(), false);
            }
            return;
        }

        boolean result = SocketSecureKey.Util.resultIsOk(protocolParams[0]);

        Tlog.v(TAG, "CurrentSetting result : " + result);

        if (mTaskCallBack != null) {
            mTaskCallBack.onSettingRecoveryResult(mSocketDataArray.getID(), result);
        }

    }
}
