package cn.com.startai.qxsdk.connect.udp;

/**
 * Created by Robin on 2019/3/20.
 * 419109715@qq.com 彬影
 */
public interface IUDPListener {

    void onUpdInitResult();

    void onUdpReceiveData(String ip ,int port);

}
