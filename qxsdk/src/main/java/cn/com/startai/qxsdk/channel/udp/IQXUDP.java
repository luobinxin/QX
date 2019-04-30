package cn.com.startai.qxsdk.channel.udp;

import java.net.DatagramPacket;

import cn.com.startai.qxsdk.channel.BaseData;
import cn.com.startai.qxsdk.channel.IConnectBusi;
import cn.com.startai.qxsdk.channel.udp.event.IQXUDPListener;

/**
 * Created by Robin on 2019/3/20.
 * 419109715@qq.com 彬影
 */
public interface IQXUDP extends IConnectBusi {

    void send(BaseData data);

    void sendDelay(BaseData data, long delay, long maxDelay);

    void broadcast(BaseData data);

    void broadcastDelay(BaseData data, long delay, long maxDelay);

    DatagramPacket getDatagramPacket(BaseData data);

    void setListener(IQXUDPListener listener);

    IQXUDPListener getListener();

}
