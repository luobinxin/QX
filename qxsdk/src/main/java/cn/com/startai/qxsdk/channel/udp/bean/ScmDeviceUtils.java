package cn.com.startai.qxsdk.channel.udp.bean;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import cn.com.startai.qxsdk.event.IOutput;
import cn.com.swain.baselib.log.Tlog;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * author: Guoqiang_Sun
 * date : 2018/8/2 0002
 * desc :
 */
public class ScmDeviceUtils {

    private IOutput output;
    private ScmDevice.OnScmCallBack mScmCallBack;

    public ScmDeviceUtils(IOutput output, ScmDevice.OnScmCallBack mScmCallBack) {
        this.output = output;
        this.mScmCallBack = mScmCallBack;
    }

    /**
     * 更新时间记录
     * <p>
     * key mac
     * obj timestamp
     */
    private final Map<String, ScmDevice> mConnectDeviceMap = Collections.synchronizedMap(new HashMap<String, ScmDevice>());


    public final void cleanMap() {
        for (Map.Entry<String, ScmDevice> tmpEntries : mConnectDeviceMap.entrySet()) {
            ScmDevice value = tmpEntries.getValue();
            if (value != null) {
                value.release();
            }
        }
        mConnectDeviceMap.clear();
    }

    private final Object synObj = new byte[1];

    public final ScmDevice getScmDevice(String mac) {
        ScmDevice scmData = mConnectDeviceMap.get(mac);

        if (scmData == null) {
            synchronized (synObj) {
                scmData = mConnectDeviceMap.get(mac);
                if (scmData == null) {
                    scmData = new ScmDevice(mac, output, mScmCallBack);
                    mConnectDeviceMap.put(mac, scmData);
                }
            }
        }

        return scmData;
    }

    final ScmDevice containScmDevice(String mac) {
        return mConnectDeviceMap.get(mac);
    }

    public final void showConnectDevice() {
        for (Map.Entry<String, ScmDevice> tmpEntries : mConnectDeviceMap.entrySet()) {
            String key = tmpEntries.getKey();
            ScmDevice value = tmpEntries.getValue();
            Tlog.e(TAG, "showConnectDevice() mac:" + key + ";  " + value.toString());
        }

    }

    public synchronized void onNetworkChange() {

        for (Map.Entry<String, ScmDevice> tmpEntries : mConnectDeviceMap.entrySet()) {
            ScmDevice value = tmpEntries.getValue();
            value.putIp(null);
        }

    }
}
