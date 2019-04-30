package cn.com.startai.qxsdk.channel.ble;

import cn.com.startai.qxsdk.channel.BaseData;
import cn.com.startai.qxsdk.channel.IConnectBusi;
import cn.com.startai.qxsdk.event.IQXCallListener;

/**
 * Created by Robin on 2019/3/20.
 * 419109715@qq.com 彬影
 */
public interface IQXBLE extends IConnectBusi {
    void send(BaseData data, IQXCallListener listener);
}
