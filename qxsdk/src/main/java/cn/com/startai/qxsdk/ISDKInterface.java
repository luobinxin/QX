package cn.com.startai.qxsdk;

import cn.com.startai.qxsdk.connect.ble.IQXBLE;
import cn.com.startai.qxsdk.connect.mqtt.IQXMqtt;
import cn.com.startai.qxsdk.connect.udp.IQXUDP;
import cn.com.startai.qxsdk.connect.udp.QXUDPImpl;

/**
 * Created by Robin on 2019/3/20.
 * 419109715@qq.com 彬影
 */
public interface ISDKInterface extends IQXBLE, IQXUDP, IQXMqtt {


}
