package cn.com.startai.qxsdk.connect.ble;

import cn.com.startai.qxsdk.connect.IConnectBusi;
import cn.com.startai.qxsdk.event.IQXCallListener;

/**
 * Created by Robin on 2019/3/20.
 * 419109715@qq.com 彬影
 */
public interface IQXBLE extends IConnectBusi {
    void send(BLEData data, IQXCallListener listener);
}
