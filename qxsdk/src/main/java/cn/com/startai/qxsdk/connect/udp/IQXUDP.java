package cn.com.startai.qxsdk.connect.udp;

import java.net.DatagramPacket;

import cn.com.startai.qxsdk.connect.IConnectBusi;

/**
 * Created by Robin on 2019/3/20.
 * 419109715@qq.com 彬影
 */
public interface IQXUDP extends IConnectBusi {

    void send(UDPData data);

    void sendDelay(UDPData data, long delay, long maxDelay);

    void broadcast(UDPData data);

    void broadcastDelay(UDPData data, long delay, long maxDelay);

    DatagramPacket getDatagramPacket(UDPData data);

}
