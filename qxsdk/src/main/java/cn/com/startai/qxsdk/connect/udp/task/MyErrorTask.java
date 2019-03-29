package cn.com.startai.qxsdk.connect.udp.task;

import cn.com.swain.baselib.log.Tlog;
import cn.com.swain.support.protocolEngine.datagram.SocketDataArray;
import cn.com.swain.support.protocolEngine.task.SocketResponseTask;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/12 0012
 * desc :
 */

public class MyErrorTask extends SocketResponseTask {

    private OnUdpTaskCallBack mTaskCallBack;

    public MyErrorTask(OnUdpTaskCallBack mTaskCallBack) {
        Tlog.e(TAG, " new MyErrorTask() ");
        this.mTaskCallBack = mTaskCallBack;
    }

    @Override
    protected void doTask(SocketDataArray mSocketDataArray) {

        Tlog.e(TAG, String.valueOf(mSocketDataArray));

        byte[] protocolParams = mSocketDataArray.getProtocolParams();

        if (protocolParams == null || protocolParams.length <= 0) {
            Tlog.e(TAG, " new MyErrorTask() params is null ");
            return;
        }

        byte protocolParam = protocolParams[0];

        if (protocolParams.length >= 3) {
            byte paramType = protocolParams[1];
            byte paramCmd = protocolParams[2];
            Tlog.e(TAG, " myType:" + Integer.toHexString(paramType) + " myCmd:" + Integer.toHexString(paramCmd));

            if (mTaskCallBack != null) {
                mTaskCallBack.onRemoveCmd(mSocketDataArray.getID(), paramType, paramCmd, mSocketDataArray.getSeq());
            }

        }

        switch (protocolParam) {

            case 0x02:
//                crc错误
                Tlog.e(TAG, " CRC error ");
                break;
            case 0x03:
//                type错误
                Tlog.e(TAG, " type error ");
                break;
            case 0x04:
//                cmd错误
                Tlog.e(TAG, " cmd error ");
                break;
            case 0x05:
//                length错误
                Tlog.e(TAG, " length error ");
                break;
            case 0x06:
                Tlog.e(TAG, " token invalid ");

                if (mTaskCallBack != null) {
                    mTaskCallBack.onTokenInvalid(mSocketDataArray.getID());
                }

                break;
            default:
                Tlog.e(TAG, " default error ");
                break;

        }


    }
}
