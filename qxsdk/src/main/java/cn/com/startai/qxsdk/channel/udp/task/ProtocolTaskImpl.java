package cn.com.startai.qxsdk.channel.udp.task;

import android.app.Application;

import cn.com.startai.qxsdk.channel.udp.SocketSecureKey;
import cn.com.startai.qxsdk.channel.udp.task.system.ControlReceiveTask;
import cn.com.startai.qxsdk.channel.udp.task.system.DeviceBindTask;
import cn.com.startai.qxsdk.channel.udp.task.system.DeviceDiscoveryTask;
import cn.com.startai.qxsdk.channel.udp.task.system.HeartbeatReceiveTask;
import cn.com.startai.qxsdk.channel.udp.task.system.RequestTokenReceiveTask;
import cn.com.startai.qxsdk.channel.udp.task.system.SleepReceiveTask;
import cn.com.swain.baselib.log.Tlog;
import cn.com.swain.support.protocolEngine.ProtocolCode;
import cn.com.swain.support.protocolEngine.datagram.SocketDataArray;
import cn.com.swain.support.protocolEngine.result.SimpleProtocolResult;
import cn.com.swain.support.protocolEngine.task.FailTaskResult;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/8 0008
 * desc :
 */

public class ProtocolTaskImpl extends SimpleProtocolResult {


    public ProtocolTaskImpl() {
    }

    private OnUdpTaskCallBack mTaskCallBack;

    private Application app;


    public ProtocolTaskImpl(OnUdpTaskCallBack mTaskCallBack, Application app) {
        this.mTaskCallBack = mTaskCallBack;
        this.app = app;
    }

    @Override
    public void onFail(FailTaskResult failTaskResult) {
        if (mTaskCallBack != null) {
            mTaskCallBack.onFail(failTaskResult);
        }
    }


    @Override
    public void onSuccess(SocketDataArray mParam) {
        try {

//        Tlog.v(TAG, " onSuccess SocketDataArray hasCode :" + mParam.hashCode());
            final byte protocolType = mParam.getProtocolType();
            final byte protocolCmd = mParam.getProtocolCmd();
            if (mTaskCallBack != null) {
                mTaskCallBack.onSuccess(mParam.getID(), protocolType, protocolCmd, mParam.getProtocolSequence());
            }
            switch (protocolType) {
                case SocketSecureKey.Type.TYPE_ERROR:
                    switch (protocolCmd) {
                        case SocketSecureKey.Cmd.CMD_ERROR:
                            new MyErrorTask(mTaskCallBack).execute(mParam);
                            break;
//                    case SocketSecureKey.Cmd.CMD_TEST:
//                        new MyTestTask(mTaskCallBack).execute(mParam);
//                        break;
                        default:
                            new ScmErrorTask(ProtocolCode.ERROR_CODE_RESOLVE_CMD, mTaskCallBack).execute(mParam);
                            break;
                    }
                    break;
                case SocketSecureKey.Type.TYPE_SYSTEM:
                    switch (protocolCmd) {
                        case SocketSecureKey.Cmd.CMD_HEARTBEAT_RESPONSE:
                            new HeartbeatReceiveTask(mTaskCallBack).execute(mParam);
                            break;
                        case SocketSecureKey.Cmd.CMD_DISCOVERY_DEVICE_RESPONSE:
                            new DeviceDiscoveryTask(mTaskCallBack, app).execute(mParam);
                            break;
                        case SocketSecureKey.Cmd.CMD_BIND_DEVICE_RESPONSE:
                            new DeviceBindTask(mTaskCallBack).execute(mParam);
                            break;
                        case SocketSecureKey.Cmd.CMD_REQUEST_TOKEN_RESPONSE:
                            new RequestTokenReceiveTask(mTaskCallBack).execute(mParam);
                            break;
                        case SocketSecureKey.Cmd.CMD_CONTROL_TOKEN_RESPONSE:
                            new ControlReceiveTask(mTaskCallBack).execute(mParam);
                            break;
                        case SocketSecureKey.Cmd.CMD_SLEEP_TOKEN_RESPONSE:
                            new SleepReceiveTask(mTaskCallBack).execute(mParam);
                            break;
//                        case SocketSecureKey.Cmd.CMD_DISCONTROL_TOKEN_RESPONSE:
//                            new DisControlReceiveTask(mTaskCallBack).execute(mParam);
//                            break;
                        default:
//                            new ScmErrorTask(ProtocolCode.ERROR_CODE_RESOLVE_CMD, mTaskCallBack).execute(mParam);
                            break;
                    }
                    break;
                case SocketSecureKey.Type.TYPE_CONTROLLER:
                    Tlog.d(TAG, "control cmd");
                    break;
                case SocketSecureKey.Type.TYPE_REPORT:
                    Tlog.d(TAG, "report cmd");
                    break;
                case SocketSecureKey.Type.TYPE_SETTING:
                    Tlog.d(TAG, "setting cmd");
                    break;
                default:
                    new ScmErrorTask(ProtocolCode.ERROR_CODE_RESOLVE_TYPE, mTaskCallBack).execute(mParam);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
