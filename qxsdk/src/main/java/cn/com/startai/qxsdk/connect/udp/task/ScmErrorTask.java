package cn.com.startai.qxsdk.connect.udp.task;

import cn.com.swain.baselib.log.Tlog;
import cn.com.swain.support.protocolEngine.datagram.SocketDataArray;
import cn.com.swain.support.protocolEngine.task.ProtocolErrorTask;

/**
 * author: Guoqiang_Sun
 * date : 2018/5/17 0017
 * desc :
 */
public class ScmErrorTask extends ProtocolErrorTask {

    private OnUdpTaskCallBack mTaskCallBack;

    public ScmErrorTask(int errorCode, OnUdpTaskCallBack mTaskCallBack) {
        super(errorCode);
        Tlog.e(TAG, " new ScmErrorTask() ");
        this.mTaskCallBack = mTaskCallBack;
    }

    @Override
    protected void doTask(SocketDataArray mSocketDataArray) {
       super.doTask(mSocketDataArray);
        if (mTaskCallBack != null) {
            mTaskCallBack.onFail(mTask);
        }
    }

}
