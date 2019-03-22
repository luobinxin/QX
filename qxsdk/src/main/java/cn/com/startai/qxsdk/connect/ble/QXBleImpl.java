package cn.com.startai.qxsdk.connect.ble;

import cn.com.startai.qxsdk.QX;
import cn.com.startai.qxsdk.connect.BaseData;
import cn.com.startai.qxsdk.connect.IConnectListener;
import cn.com.startai.qxsdk.connect.mqtt.IQXMqtt;
import cn.com.startai.qxsdk.connect.udp.QXUDPImpl;

/**
 * Created by Robin on 2019/3/21.
 * 419109715@qq.com 彬影
 */
public class QXBleImpl implements IQXBLE {
    private static volatile QXBleImpl instance;

    private QXBleImpl() {
    }

    public static void registerInstance() {
        if (instance == null) {
            synchronized (QXUDPImpl.class) {
                if (instance == null) {
                    instance = new QXBleImpl();

                    instance.init();
                }
            }
        }
        QX.setQxBle(instance);
    }


    @Override
    public void init() {

    }

    @Override
    public void release() {

    }

    @Override
    public void doSend(BaseData data) {

    }

}
