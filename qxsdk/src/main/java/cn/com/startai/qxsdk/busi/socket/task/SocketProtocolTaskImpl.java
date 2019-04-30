package cn.com.startai.qxsdk.busi.socket.task;

import android.app.Application;

import cn.com.startai.qxsdk.busi.socket.ISocketTaskCallback;
import cn.com.startai.qxsdk.channel.udp.SocketSecureKey;
import cn.com.startai.qxsdk.channel.udp.task.ProtocolTaskImpl;
import cn.com.swain.baselib.log.Tlog;
import cn.com.swain.support.protocolEngine.datagram.SocketDataArray;
import cn.com.swain.support.protocolEngine.task.FailTaskResult;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/8 0008
 * desc :
 */

public class SocketProtocolTaskImpl extends ProtocolTaskImpl {



    private final ISocketTaskCallback mTaskCallBack;

    private final Application app;

    public SocketProtocolTaskImpl(ISocketTaskCallback mTaskCallBack, Application app) {
        this.mTaskCallBack = mTaskCallBack;
        this.app = app;
    }

    @Override
    public void onFail(FailTaskResult failTaskResult) {
        super.onFail(failTaskResult);
    }


    @Override
    public void onSuccess(SocketDataArray mParam) {

        try {

            final byte protocolType = mParam.getProtocolType();
            final byte protocolCmd = mParam.getProtocolCmd();

            switch (protocolType) {
                case SocketSecureKey.Type.TYPE_ERROR:
                    switch (protocolCmd) {
                        case SocketSecureKey.Cmd.CMD_ERROR:
                            break;
                        default:
                            break;
                    }
                    break;
                case SocketSecureKey.Type.TYPE_SYSTEM:
                    switch (protocolCmd) {
                        case SocketSecureKey.Cmd.CMD_UPDATE_RESPONSE:
                            new UpdateReceiveTask(mTaskCallBack).doTask(mParam);
                            break;
                        case SocketSecureKey.Cmd.CMD_SET_RECOVERY_SCM_RESPONSE:
                            new RecoverySettingReceiveTask(mTaskCallBack).doTask(mParam);
                            break;
                        case SocketSecureKey.Cmd.CMD_RENAME_RESPONSE:
                            new RenameReceiveTask(mTaskCallBack).doTask(mParam);
                            break;
                    }
                    break;
                case SocketSecureKey.Type.TYPE_CONTROLLER:
                    Tlog.d(TAG, "control cmd 1");
                    break;
                case SocketSecureKey.Type.TYPE_REPORT:
                    Tlog.d(TAG, "report cmd 1");
                    break;
                case SocketSecureKey.Type.TYPE_SETTING:
                    Tlog.d(TAG, "setting cmd 1");
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
